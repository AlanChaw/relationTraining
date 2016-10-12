package Training.ProcessWeighting;

import Training.Filters.WeightingFilter;
import Training.Model.PointWordExtend;
import Training.Model.WeightingTask;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by alan on 16/9/10.
 */
public class TF_IDF implements WeightingFilter {


    public int handleWeighting(WeightingTask task) {
        WeightingFilter TFFilter = new PureTF();
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
