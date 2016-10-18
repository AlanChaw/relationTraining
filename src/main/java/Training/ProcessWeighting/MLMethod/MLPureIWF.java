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
public class MLPureIWF extends MLWeighting implements WeightingFilterML {

    @Override
    public int handleWeighting(WeightingTask task) {
        return super.handleWeighting(task);
    }


    @Override
    protected void doStatistics(EntityPairExtend entityPairExtend, List<MatchSentence> allSentences) {
        MLPureTF mlPureTF = new MLPureTF();
        mlPureTF.doStatistics(entityPairExtend, allSentences);
        int sum = 0;
        for (PointWordExtend pointWordExtend : entityPairExtend.getPointWordExtendList()){
            sum += pointWordExtend.getAppearCount();
        }
        for (PointWordExtend pointWordExtend : entityPairExtend.getPointWordExtendList()){
            if (pointWordExtend.getAppearCount() == 0){
                pointWordExtend.setInverseWordFrequency(0.0);
                pointWordExtend.setStatisticValue(0.0);
                continue;
            }
            Double IWF = (double)sum / pointWordExtend.getAppearCount();
            IWF = Math.log(IWF);
            pointWordExtend.setInverseWordFrequency(IWF);
            //纯IWF方法, 把统计值直接设置为IWF值
            pointWordExtend.setStatisticValue(IWF);
        }
    }
}
