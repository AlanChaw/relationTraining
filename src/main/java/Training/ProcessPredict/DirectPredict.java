package Training.ProcessPredict;

import DealFile.Doc;
import DealFile.Lemma;
import DealFile.OriginFile;
import Training.EntityPairExtend;
import Training.PointWordExtend;
import Training.PredictFliter;
import Training.PredictTask;

import java.util.List;

/**
 * Created by alan on 16/8/10.
 */
public class DirectPredict implements PredictFliter {

    PredictTask predictTask;

    public int handlePredict(PredictTask task) {
        this.predictTask = task;

        for (EntityPairExtend entityPairExtend : predictTask.getEntityPairsToPredict()){
            doPredict(entityPairExtend);
        }


        return 0;
    }

    private void doPredict(EntityPairExtend entityPairExtend){
        Integer identifi = Integer.valueOf(entityPairExtend.getEntityPair().getIdentifi());
        OriginFile originFile = predictTask.getOriginFileList().get(identifi);
        List<Doc> docs = originFile.getDocs();

        Double cooperateValue = 0.0;
        Double competeValue = 0.0;

        for (Doc doc : docs){
            for (Lemma docLemma : doc.getLemmaList()){
                for (PointWordExtend pointWordExtend : predictTask.getCompeteExtendedPointWords()){
                    if (pointWordExtend.getAppearCount() == 0){
                        continue;
                    }
                    if (pointWordExtend.getPointWord().getLemma().equals(docLemma.getLemma())){
                        competeValue += pointWordExtend.getStatisticValue();
                    }
                }
                for (PointWordExtend pointWordExtend : predictTask.getCooperateExtendedPointWords()){
                    if (pointWordExtend.getAppearCount() == 0){
                        continue;
                    }
                    if (pointWordExtend.getPointWord().getLemma().equals(docLemma.getLemma())){
                        cooperateValue += pointWordExtend.getStatisticValue();
                    }
                }
            }
        }

        if (competeValue == cooperateValue){
            entityPairExtend.setPredictValue(0);
        }else if (competeValue > cooperateValue){
            entityPairExtend.setPredictValue(-1);
        }else if (cooperateValue > competeValue){
            entityPairExtend.setPredictValue(1);
        }

        System.out.println(entityPairExtend.toString());
        System.out.println("竞争值: " + competeValue + "\t" + "合作值: " + cooperateValue + "\n");
    }


}