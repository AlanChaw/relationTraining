package Training.ProcessTraining;

import Training.Filters.TrainingFilter;
import Training.Model.TrainingTask;
import WeightingKNN.WKNN;
import com.mathworks.toolbox.javabuilder.MWNumericArray;

import java.util.List;

/**
 * Created by alan on 10/20/16.
 */
public class Wknn extends TrainingMethod implements TrainingFilter {

    public int handleWeighting(TrainingTask task) {
        return super.handleTraining(task);
    }

    @Override
    protected void doTraining(List<List<Double>> X_Matrix, List<Integer> Y_Matrix) {
        super.doTraining(X_Matrix, Y_Matrix);

        MWNumericArray output = null;
        Object[] result = null;
        try {
            WKNN wknn = null;
            wknn = new WKNN();
            parameters.put("wknn", wknn);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
