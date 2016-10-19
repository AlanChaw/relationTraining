package Training.ProcessTraining;

import SLR.SimpleLogisticRegression;
import Training.Filters.TrainingFilter;
import Training.Model.TrainingTask;
import com.mathworks.toolbox.javabuilder.MWNumericArray;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.List;

/**
 * Created by alan on 10/19/16.
 */
public class QuadraticSVM extends TrainingMethod implements TrainingFilter {
    public int handleTraining(TrainingTask task) {
        return super.handleTraining(task);
    }


    @Override
    protected void doTraining(List<List<Double>> X_Matrix, List<Integer> Y_Matrix) {
        super.doTraining(X_Matrix, Y_Matrix);


    }


}
