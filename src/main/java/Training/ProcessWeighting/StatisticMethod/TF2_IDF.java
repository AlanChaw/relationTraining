package Training.ProcessWeighting.StatisticMethod;

import Training.Filters.WeightingFilter;
import Training.Model.PointWordExtend;
import Training.Model.WeightingTask;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by alan on 16/9/11.
 */
public class TF2_IDF implements WeightingFilter {


    public int handleWeighting(WeightingTask task) {

        WeightingFilter TFFilter = new PureTF2();
        WeightingFilter IDFFilter = new PureIDF();

        TFFilter.handleWeighting(task);
        IDFFilter.handleWeighting(task);

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
