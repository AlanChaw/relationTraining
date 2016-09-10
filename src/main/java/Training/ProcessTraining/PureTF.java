package Training.ProcessTraining;

/**
 * Created by alan on 16/7/30.
 */

import DealFile.Model.Doc;
import DealFile.Model.EntityPair;
import DealFile.Model.Lemma;
import DealFile.Model.OriginFile;
import Training.Filters.TrainingFilter;
import Training.Model.MatchSentence;
import Training.Model.PointWordExtend;
import Training.Model.TrainingTask;

import java.util.ArrayList;
import java.util.List;

/**
 * v1.0 最简单的训练方法,用实体所在的句子,直接找出wordnet关键词
 */
public class PureTF implements TrainingFilter {
    public static int WINDOWLENGTH;

    protected TrainingTask trainingTask;

    public int handleTraining(TrainingTask task) {
        this.trainingTask = task;

        Integer sentencesNum = 0;

        for (EntityPair entityPair : task.getTrainingSet()){
            Integer identifi = Integer.valueOf(entityPair.getIdentifi());
            OriginFile originFile = HelpMethods.fileJsonToModel(task.getOriginFileList().get(identifi));
            for (Doc doc : originFile.getDocs()){
                List<MatchSentence> sentences = HelpMethods.findSentencesInDocs(doc, entityPair);
                sentencesNum += sentences.size();
                doTheTraining(sentences, entityPair.getRelation());

            }

        }
        doStatistic(sentencesNum);
        return 0;
    }

    /**
     * 统计指示词在sentence中出现的次数
     * @param sentences
     * @param relation
     */
    public void doTheTraining(List<MatchSentence> sentences, Integer relation){
        List<PointWordExtend> pointWordList = new ArrayList<PointWordExtend>();

        pointWordList = trainingTask.getPointWordExtendList();

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
    public void doStatistic(Integer sentencesNum) {

        System.out.println("两实体共同出现的句子: " + sentencesNum + "条");
        for (PointWordExtend pointWordExtend : trainingTask.getPointWordExtendList()) {
            if (pointWordExtend.getAppearCount() > 0) {
                System.out.println("词汇 " + pointWordExtend.getPointWord().getLemma() + " 出现: " + pointWordExtend.getAppearCount() + " 次");
            }
        }
        System.out.println("------------------");

        Integer wordNum = 0;
        for (PointWordExtend pointWordExtend : trainingTask.getPointWordExtendList()) {
            wordNum += pointWordExtend.getAppearCount();
        }

        for (PointWordExtend pointWordExtend : trainingTask.getPointWordExtendList()) {
            if (pointWordExtend.getAppearCount() > 0){
                Double statisticValue = (double)pointWordExtend.getAppearCount() / wordNum;
                pointWordExtend.setStatisticValue(statisticValue);

                System.out.println(pointWordExtend.toString());
            }

        }


    }


}
