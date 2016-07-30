package Training.ProcessTraining;

/**
 * Created by alan on 16/7/30.
 */

import DealFile.Doc;
import DealFile.EntityPair;
import DealFile.Lemma;
import DealFile.OriginFile;
import Training.MatchSentence;
import Training.PointWordExtend;
import Training.Task;
import Training.TrainingFliter;

import java.util.ArrayList;
import java.util.List;

/**
 * v1.0 最简单的训练方法,用实体所在的句子,直接找出wordnet关键词
 */
public class DirectSearchTraining implements TrainingFliter {
    public static int WINDOWLENGTH;

    private Task task;

    public int handleTraining(Task task) {
        this.task = task;

        Integer sentencesNum = 0;

        for (EntityPair entityPair : task.getTrainingSet()){
            Integer identifi = Integer.valueOf(entityPair.getIdentifi());
            OriginFile originFile = task.getOriginFileList().get(identifi);
            for (Doc doc : originFile.getDocs()){
                List<MatchSentence> sentences = findSentencesInDocs(doc, entityPair);
                sentencesNum += sentences.size();
                doTheTraining(sentences, entityPair.getRelation());

            }

        }
        doStatistic(sentencesNum);
        return 0;
    }


    /**********************核心部分*******************************/

    /**
     * 在某一关系对所出现的某一个文档中,找出在给定窗口长度下关系对中两个实体共同出现的句子
     * @param doc   给定的文档
     * @param entityPair    某一特定的实体对
     * @return 在该文档中所有匹配的句子
     */
    public List<MatchSentence> findSentencesInDocs(Doc doc, EntityPair entityPair){
        List<MatchSentence> sentences = new ArrayList<MatchSentence>();

        //遍历整个文档,直接检索,找到实体词出现的位置
        for (int i = 0; i < doc.getLemmaList().size(); i++){
            Lemma lemma = doc.getLemmaList().get(i);
            if (lemma.getLemma().equals(entityPair.getEntityName_1())){
                //该词窗口的下界
                Integer lowerBound = (i >= WINDOWLENGTH) ? (i - WINDOWLENGTH) : 0;
                //该词窗口的上界(实际是下标,便于操作)
                Integer upperBound = ((i + WINDOWLENGTH + 1) <= doc.getLemmaList().size()) ? (i + WINDOWLENGTH) : (doc.getLemmaList().size() - 1);

                //设置句子
                MatchSentence sentence = new MatchSentence();
                sentence.setRelation(entityPair.getRelation());
                for (int j = lowerBound; j <= upperBound; j++){
                    sentence.addLemma(doc.getLemmaList().get(j));
                }

                //检索句子中是否有另一个实体词,如果有则保留
                for (int j = 0; j < sentence.getLemmas().size(); j++){
                    if (sentence.getLemmas().get(j).getLemma().equals(entityPair.getEntityName_2()))
                        sentences.add(sentence);
                }
            }

        }
        return sentences;
    }

    /**
     * 统计指示词在sentence中出现的次数
     * @param sentences
     * @param relation
     */
    public void doTheTraining(List<MatchSentence> sentences, Integer relation){
        List<PointWordExtend> pointWordList = new ArrayList<PointWordExtend>();
//        if (relation < 0){
//            pointWordList = competeExtendedPonintWords;
//        }
//        else {
//            pointWordList = cooperateExtendedPointWords;
//        }
        pointWordList = task.getPointWordExtendList();

        for (MatchSentence sentence : sentences){
            for (Lemma lemma : sentence.getLemmas()){
                for (PointWordExtend word : pointWordList){
                    if (word.getPointWord().getLemma().equals(lemma.getLemma())){
                        word.setAppearCount(word.getAppearCount() + 1);
//                        System.out.println("发现指示词: " + lemma.getLemma());
                    }

                }
            }

        }

    }

    /**
     * 经过训练后,对每个指示词在句子中出现的频率统计,计算statisticValue
     */
    public void doStatistic(Integer sentencesNum){

        System.out.println("两实体共同出现的句子: " + sentencesNum + "条");
        for (PointWordExtend pointWordExtend : task.getPointWordExtendList()){
            if (pointWordExtend.getAppearCount() > 0){
                System.out.println("词汇 " + pointWordExtend.getPointWord().getLemma() + " 出现: " + pointWordExtend.getAppearCount() + " 次");
                

            }
        }

        System.out.println("------------------");

    }


}
