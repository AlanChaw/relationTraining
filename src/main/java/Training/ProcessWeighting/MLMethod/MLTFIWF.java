package Training.ProcessWeighting.MLMethod;

import Training.Filters.WeightingFilterML;
import Training.Model.EntityPairExtend;
import Training.Model.MatchSentence;
import Training.Model.PointWordExtend;
import Training.Model.WeightingTask;

import java.util.List;

/**
 * Created by alan on 10/18/16.
 */
public class MLTFIWF extends MLWeighting implements WeightingFilterML {
    public int handleWeighting(WeightingTask task) {
        return super.handleWeighting(task);
    }

    @Override
    protected void doStatistics(EntityPairExtend entityPairExtend, List<MatchSentence> allSentences){
        MLPureIWF mlPureIWF = new MLPureIWF();
        mlPureIWF.doStatistics(entityPairExtend, allSentences);

        for (PointWordExtend pointWordExtend : entityPairExtend.getPointWordExtendList()){
            Double TF_IWF = pointWordExtend.getTermFrequency() * pointWordExtend.getInverseWordFrequency();
            pointWordExtend.setStatisticValue(TF_IWF);
        }

    }



}
