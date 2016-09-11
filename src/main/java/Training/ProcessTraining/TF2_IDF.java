package Training.ProcessTraining;

import Training.Filters.TrainingFilter;
import Training.Model.PointWordExtend;
import Training.Model.TrainingTask;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by alan on 16/9/11.
 */
public class TF2_IDF implements TrainingFilter {


    public int handleTraining(TrainingTask task) {

        TrainingFilter TFFilter = new PureTF2();
        TrainingFilter IDFFilter = new PureIDF();

        TFFilter.handleTraining(task);
        IDFFilter.handleTraining(task);

        List<PointWordExtend> pointWordExtendList = new ArrayList<PointWordExtend>();
        pointWordExtendList.addAll(task.getPointWordExtendListCompete());
        pointWordExtendList.addAll(task.getPointWordExtendListCooperate());

        for (PointWordExtend pointWordExtend : pointWordExtendList){
            Double tf_idf = pointWordExtend.getTermFrequency() * pointWordExtend.getInverseDocumentFrequency();
            pointWordExtend.setStatisticValue(tf_idf);
        }

        return 0;
    }



}
