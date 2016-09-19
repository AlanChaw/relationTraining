package Training.ProcessTraining;

import Training.Filters.TrainingFilter;
import Training.Model.PointWordExtend;
import Training.Model.TrainingTask;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by alan on 16/9/11.
 */
public class TF_IWF implements TrainingFilter {
    public int handleTraining(TrainingTask task) {

        TrainingFilter TFFilter = new PureTF();
        TrainingFilter IWFFilter = new PureIWF();

        TFFilter.handleTraining(task);
        IWFFilter.handleTraining(task);

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
