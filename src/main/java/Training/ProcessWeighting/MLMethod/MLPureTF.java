package Training.ProcessWeighting.MLMethod;

import DealFile.Model.Doc;
import DealFile.Model.EntityPair;
import DealFile.Model.Lemma;
import DealFile.Model.OriginFile;
import Training.Filters.WeightingFilterML;
import Training.Model.EntityPairExtend;
import Training.Model.MatchSentence;
import Training.Model.PointWordExtend;
import Training.Model.WeightingTask;
import Training.ProcessWeighting.HelpMethods;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by alan on 16/10/13.
 */
public class MLPureTF implements WeightingFilterML{
    private List<EntityPairExtend> entityPairExtends;

    protected WeightingTask weightingTask;
    public List<EntityPairExtend> handleWeighting(WeightingTask task) {
        this.entityPairExtends = new ArrayList<EntityPairExtend>();
        this.weightingTask = task;

        handleWeightingCompete();
        handleWeightingCooperate();

        return this.entityPairExtends;
    }

    protected void handleWeightingCompete(){
        for (EntityPair entityPair : weightingTask.getEntityPairSetCompete()){
            Integer identifi = Integer.valueOf(entityPair.getIdentifi());
            OriginFile originFile = HelpMethods.fileJsonToModel(weightingTask.getOriginFileList().get(identifi));
            List<MatchSentence> allSentences = new ArrayList<MatchSentence>();
            for (Doc doc : originFile.getDocs()){
                List<MatchSentence> sentences = HelpMethods.findSentencesInDoc(doc, entityPair);
                allSentences.addAll(sentences);
            }
            EntityPairExtend entityPairExtend = doTheTraining(allSentences, weightingTask.getPointWordExtendListCompete(), entityPair);
            this.entityPairExtends.add(entityPairExtend);
        }


    }


    protected void handleWeightingCooperate(){
        for (EntityPair entityPair : weightingTask.getEntityPairSetCooperate()){
            Integer identifi = Integer.valueOf(entityPair.getIdentifi());
            OriginFile originFile = HelpMethods.fileJsonToModel(weightingTask.getOriginFileList().get(identifi));
            List<MatchSentence> allSentences = new ArrayList<MatchSentence>();
            for (Doc doc : originFile.getDocs()){
                List<MatchSentence> sentences = HelpMethods.findSentencesInDoc(doc, entityPair);
                allSentences.addAll(sentences);
            }
            EntityPairExtend entityPairExtend = doTheTraining(allSentences, weightingTask.getPointWordExtendListCooperate(), entityPair);
            this.entityPairExtends.add(entityPairExtend);

        }

    }

    /**
     * 统计指示词在sentence中出现的次数,对每个关系对 都要训练一套新的指示词权重集合
     * @param sentences
     * @param relation
     */
    protected EntityPairExtend doTheTraining(List<MatchSentence> sentences, List<PointWordExtend> pointWordList, EntityPair entityPair){
        List<PointWordExtend> pointwordInEntityPair = new ArrayList<PointWordExtend>();
//        pointwordInEntityPair.addAll(pointWordList);
        Iterator<PointWordExtend> iterator = pointWordList.iterator();
        while (iterator.hasNext()){
            pointwordInEntityPair.add(iterator.next().clone());
        }

        for (MatchSentence sentence : sentences){
            for (Lemma lemma : sentence.getLemmas()){
                for (PointWordExtend word : pointwordInEntityPair){
                    if (word.getPointWord().getLemma().equals(lemma.getLemma())){
                        word.setAppearCount(word.getAppearCount() + 1);
//                        System.out.println("发现指示词: " + lemma.getLemma());
                    }

                }
            }

        }

        EntityPairExtend entityPairExtend = new EntityPairExtend();
        entityPairExtend.setEntityPair(entityPair);
        doStatistic(pointwordInEntityPair);

        entityPairExtend.setPointWordExtendList(pointwordInEntityPair);
        if (entityPairExtend.getEntityPair().getRelation() == 0){
            pointwordInEntityPair.addAll(this.weightingTask.getPointWordExtendListCooperate());
            entityPairExtend.setPointWordExtendList(pointwordInEntityPair);
        }else {
            List<PointWordExtend> pointWordCooperate = new ArrayList<PointWordExtend>();
            pointWordCooperate.addAll(this.weightingTask.getPointWordExtendListCompete());
            pointWordCooperate.addAll(pointwordInEntityPair);
            entityPairExtend.setPointWordExtendList(pointWordCooperate);
        }


        return entityPairExtend;
    }

    protected void doStatistic(List<PointWordExtend> pointWordExtends){
        int sum = 0;

        for (PointWordExtend pointWordExtend : pointWordExtends){
            sum += pointWordExtend.getAppearCount();
        }
        for (PointWordExtend pointWordExtend : pointWordExtends){
            if (sum == 0){
                pointWordExtend.setTermFrequency(0.0);
            }else {
                pointWordExtend.setTermFrequency((double) pointWordExtend.getAppearCount() / sum);
            }
            //纯tf方法,将统计值直接设为tf值
            pointWordExtend.setStatisticValue(pointWordExtend.getTermFrequency());
        }

    }

}
