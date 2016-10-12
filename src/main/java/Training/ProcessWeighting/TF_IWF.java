package Training.ProcessWeighting;

import Training.Filters.WeightingFilter;
import Training.Model.PointWordExtend;
import Training.Model.WeightingTask;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by alan on 16/9/11.
 */
public class TF_IWF implements WeightingFilter {
    public int handleWeighting(WeightingTask task) {

        WeightingFilter TFFilter = new PureTF();
        WeightingFilter IWFFilter = new PureIWF();

        TFFilter.handleWeighting(task);
        IWFFilter.handleWeighting(task);

        List<PointWordExtend> pointWordExtendList = new ArrayList<PointWordExtend>();
        pointWordExtendList.addAll(task.getPointWordExtendListCompete());
        pointWordExtendList.addAll(task.getPointWordExtendListCooperate());

        for (PointWordExtend pointWordExtend : pointWordExtendList){
            Double tf_iwf = pointWordExtend.getTermFrequency() * pointWordExtend.getInverseWordFrequency();
            pointWordExtend.setStatisticValue(tf_iwf);
        }

        return 0;
    }


}
