package Training.ProcessWeighting.MLMethod;

import DealFile.Model.Doc;
import DealFile.Model.EntityPair;
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
 * Created by alan on 16/10/17.
 */
public class MLWeighting implements WeightingFilterML {
    protected List<PointWordExtend> pointWordExtends;
    protected WeightingTask weightingTask;

    public int handleWeighting(WeightingTask task) {
        this.pointWordExtends = new ArrayList<PointWordExtend>();
        this.weightingTask = task;

        generateWordAndEntitySet();

        doWeightings(competeEntityPairsWithWeightings);
        doWeightings(cooperateEntityPairsWithWeightings);

        return 1;
    }

    protected void generateWordAndEntitySet(){
        this.pointWordExtends.addAll(this.weightingTask.getPointWordExtendListCompete());
        this.pointWordExtends.addAll(this.weightingTask.getPointWordExtendListCooperate());

        for (EntityPair entityPair : weightingTask.getEntityPairSetCompete()){
            EntityPairExtend entityPairExtend = new EntityPairExtend();
            entityPairExtend.setEntityPair(entityPair);
            generatePointwordInEntity(entityPairExtend);
            competeEntityPairsWithWeightings.add(entityPairExtend);
        }

        for (EntityPair entityPair : weightingTask.getEntityPairSetCooperate()){
            EntityPairExtend entityPairExtend = new EntityPairExtend();
            entityPairExtend.setEntityPair(entityPair);
            generatePointwordInEntity(entityPairExtend);
            cooperateEntityPairsWithWeightings.add(entityPairExtend);
        }

    }

    protected void generatePointwordInEntity(EntityPairExtend entityPairExtend){
        //这里必须用深复制
        List<PointWordExtend> pointwordInEntityPair = new ArrayList<PointWordExtend>();
        Iterator<PointWordExtend> iterator = this.pointWordExtends.iterator();
        while (iterator.hasNext()){
            pointwordInEntityPair.add(iterator.next().clone());
        }
        entityPairExtend.setPointWordExtendList(pointwordInEntityPair);
    }

    protected void doWeightings(List<EntityPairExtend> entityPairExtends){
        for (EntityPairExtend entityPairExtend : entityPairExtends){
            Integer identify = Integer.valueOf(entityPairExtend.getEntityPair().getIdentifi());
            OriginFile originFile = HelpMethods.fileJsonToModel(weightingTask.getOriginFileList().get(identify));
            List<MatchSentence> allSentences = new ArrayList<MatchSentence>();
            for (Doc doc : originFile.getDocs()){
                List<MatchSentence> sentences = HelpMethods.findSentencesInDoc(doc, entityPairExtend.getEntityPair());
                allSentences.addAll(sentences);
            }

            doStatistics(entityPairExtend, allSentences);
        }

    }
    protected void doStatistics(EntityPairExtend entityPairExtend, List<MatchSentence> allSentences) {

    }


}
