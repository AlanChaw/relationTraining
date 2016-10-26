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
 * Created by alan on 10/26/16.
 */
public class MLBDC implements WeightingFilterML {
    private WeightingTask weightingTask;
    protected List<PointWordExtend> pointWordExtends;

    public int handleWeighting(WeightingTask task) {
        this.weightingTask = task;
        List<MatchSentence> allSentencesCompete = new ArrayList<MatchSentence>();
        List<MatchSentence> allSentencesCooperate = new ArrayList<MatchSentence>();

        List<EntityPair> allEntityPairs = new ArrayList<EntityPair>();
        allEntityPairs.addAll(this.weightingTask.getEntityPairSetCompete());
        allEntityPairs.addAll(this.weightingTask.getEntityPairSetCooperate());

        this.pointWordExtends = new ArrayList<PointWordExtend>();
        this.pointWordExtends.addAll(this.weightingTask.getPointWordExtendListCompete());
        this.pointWordExtends.addAll(this.weightingTask.getPointWordExtendListCooperate());

        for (EntityPair entityPair : weightingTask.getEntityPairSetCompete()){
            Integer identifi = Integer.valueOf(entityPair.getIdentifi());
            OriginFile originFile = HelpMethods.fileJsonToModel(weightingTask.getOriginFileList().get(identifi));
            for (Doc doc : originFile.getDocs()){
                List<MatchSentence> sentences = HelpMethods.findSentencesInDoc(doc, entityPair);
                allSentencesCompete.addAll(sentences);
            }
        }
        calcuateNumInCompete(allSentencesCompete);

        for (EntityPair entityPair : weightingTask.getEntityPairSetCooperate()){
            Integer identifi = Integer.valueOf(entityPair.getIdentifi());
            OriginFile originFile = HelpMethods.fileJsonToModel(weightingTask.getOriginFileList().get(identifi));
            for (Doc doc : originFile.getDocs()){
                List<MatchSentence> sentences = HelpMethods.findSentencesInDoc(doc, entityPair);
                allSentencesCooperate.addAll(sentences);
            }
        }
        caculateNumInCooperate(allSentencesCooperate);


        caculateWeightingEntropy(pointWordExtends);

        doWeighting(allEntityPairs);


        return 1;
    }

    private void calcuateNumInCompete(List<MatchSentence> allSentences){
        for (PointWordExtend pointWordExtend : this.pointWordExtends) {
            for (MatchSentence sentence : allSentences){
                for (Lemma lemma : sentence.getLemmas()){
                    if (lemma.getLemma().equals(pointWordExtend.getPointWord().getLemma())){
                        pointWordExtend.setCcompete(pointWordExtend.getCcompete() + 1);
                    }
                }
            }
        }
    }

    private void caculateNumInCooperate(List<MatchSentence> allSentences){
        for (PointWordExtend pointWordExtend : this.pointWordExtends) {
            for (MatchSentence sentence : allSentences){
                for (Lemma lemma : sentence.getLemmas()){
                    if (lemma.getLemma().equals(pointWordExtend.getPointWord().getLemma())){
                        pointWordExtend.setCcooperate(pointWordExtend.getCcooperate() + 1);
                    }
                }
            }
        }
    }

    private void doWeighting(List<EntityPair> allEntityPairs){
        for (EntityPair entityPair : allEntityPairs){
            List<MatchSentence> allsentences = new ArrayList<MatchSentence>();
            Integer identifi = Integer.valueOf(entityPair.getIdentifi());
            OriginFile originFile = HelpMethods.fileJsonToModel(weightingTask.getOriginFileList().get(identifi));
            for (Doc doc : originFile.getDocs()){
                List<MatchSentence> sentences = HelpMethods.findSentencesInDoc(doc, entityPair);
                allsentences.addAll(sentences);
            }
            doStatistic(entityPair, allsentences);
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

    private void caculateWeightingEntropy(List<PointWordExtend> pointWordExtends){
        for (PointWordExtend pointWordExtend : pointWordExtends){
            Integer cCompete = pointWordExtend.getCcompete();
            Integer cCooperate = pointWordExtend.getCcooperate();
            Integer cw = cCompete + cCooperate;
            if (cw == 0){
                pointWordExtend.setWeight(0.0);
                continue;
            }
            if (cw == cCompete || cw == cCooperate){
                pointWordExtend.setWeight(Math.exp(1));
            }
            Double weightingEntropy = Math.log10((double)cCompete / cw) / Math.log10(2) + Math.log10((double)cCooperate / cw) / Math.log10(2);
            weightingEntropy = (double)1 / (Math.exp(-1 * weightingEntropy - 1));
            pointWordExtend.setWeight(weightingEntropy);
        }
    }

    private void doStatistic(EntityPair entityPair, List<MatchSentence> sentences){
        EntityPairExtend entityPairExtend = new EntityPairExtend();
        entityPairExtend.setEntityPair(entityPair);
        generatePointwordInEntity(entityPairExtend);
        for (PointWordExtend pointWordExtend : entityPairExtend.getPointWordExtendList()){
            for (MatchSentence sentence : sentences){
                for (Lemma lemma : sentence.getLemmas()){
                    if (lemma.getLemma().equals(pointWordExtend.getPointWord().getLemma())){
                        pointWordExtend.setWeightInEntity(pointWordExtend.getWeight());
                    }
                }
            }
        }

        for (PointWordExtend pointWordExtend : entityPairExtend.getPointWordExtendList()){
            pointWordExtend.setStatisticValue(pointWordExtend.getWeightInEntity());
        }

        if (Integer.valueOf(entityPairExtend.getEntityPair().getRelation()) == 0){
            competeEntityPairsWithWeightings.add(entityPairExtend);
        }else {
            cooperateEntityPairsWithWeightings.add(entityPairExtend);
        }

    }


}
