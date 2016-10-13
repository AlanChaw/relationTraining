package Training.ProcessWeighting.StatisticMethod;

/**
 * Created by alan on 16/7/30.
 */

import DealFile.Model.Doc;
import DealFile.Model.EntityPair;
import DealFile.Model.Lemma;
import DealFile.Model.OriginFile;
import Training.Filters.WeightingFilter;
import Training.Model.MatchSentence;
import Training.Model.PointWordExtend;
import Training.Model.WeightingTask;
import Training.ProcessWeighting.HelpMethods;

import java.util.List;

/**
 * 最简单的训练方法,用实体所在的句子,直接找出wordnet关键词
 */
public class PureTF implements WeightingFilter {
    public static int WINDOWLENGTH;

    protected WeightingTask weightingTask;

    public int handleWeighting(WeightingTask task) {
        this.weightingTask = task;
        handleTrainingCompete(weightingTask.getTrainingSetCompete());
        handleTrainingCooperate(weightingTask.getTrainingSetCooperate());
        return 0;
    }

    protected void handleTrainingCompete(List<EntityPair> trainingSetCompete){

        Integer sentencesNum = 0;

        for (EntityPair entityPair : trainingSetCompete){
            Integer identifi = Integer.valueOf(entityPair.getIdentifi());
            OriginFile originFile = HelpMethods.fileJsonToModel(weightingTask.getOriginFileList().get(identifi));
            for (Doc doc : originFile.getDocs()){
                List<MatchSentence> sentences = HelpMethods.findSentencesInDoc(doc, entityPair);
                sentencesNum += sentences.size();
                doTheTraining(sentences, weightingTask.getPointWordExtendListCompete());

            }

        }
        doStatistic(sentencesNum, weightingTask.getPointWordExtendListCompete());

    }

    protected void handleTrainingCooperate(List<EntityPair> trainingSetCooperate){
        Integer sentencesNum = 0;

        for (EntityPair entityPair : trainingSetCooperate){
            Integer identifi = Integer.valueOf(entityPair.getIdentifi());
            OriginFile originFile = HelpMethods.fileJsonToModel(weightingTask.getOriginFileList().get(identifi));
            for (Doc doc : originFile.getDocs()){
                List<MatchSentence> sentences = HelpMethods.findSentencesInDoc(doc, entityPair);
                sentencesNum += sentences.size();
                doTheTraining(sentences, weightingTask.getPointWordExtendListCooperate());

            }

        }
        doStatistic(sentencesNum, weightingTask.getPointWordExtendListCooperate());
    }

    /**
     * 统计指示词在sentence中出现的次数
     * @param sentences
     * @param relation
     */
    protected void doTheTraining(List<MatchSentence> sentences, List<PointWordExtend> pointWordList){
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
    protected void doStatistic(Integer sentencesNum, List<PointWordExtend> pointWordExtendList) {

        System.out.println("两实体共同出现的句子: " + sentencesNum + "条");
        for (PointWordExtend pointWordExtend : pointWordExtendList) {
            if (pointWordExtend.getAppearCount() > 0) {
                System.out.println("词汇 " + pointWordExtend.getPointWord().getLemma() + " 出现: " + pointWordExtend.getAppearCount() + " 次");
            }
        }
        System.out.println("------------------");

        Integer wordNum = 0;
        for (PointWordExtend pointWordExtend : pointWordExtendList) {
            wordNum += pointWordExtend.getAppearCount();
        }
        caculateValue(pointWordExtendList, wordNum);

    }

    protected void caculateValue(List<PointWordExtend> list, Integer wordNum){
        for (PointWordExtend pointWordExtend : list) {
            if (pointWordExtend.getAppearCount() > 0){
                Double termFrequency = (double)pointWordExtend.getAppearCount() / wordNum;
                pointWordExtend.setTermFrequency(termFrequency);
                //把统计值直接设置为TF的值
                pointWordExtend.setStatisticValue(termFrequency);
                System.out.println(pointWordExtend.toString());
            }

        }
    }


}
