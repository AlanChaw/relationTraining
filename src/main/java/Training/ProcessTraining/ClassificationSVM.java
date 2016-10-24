package Training.ProcessTraining;

import CSVM.SVM;
import QuadraticSVM.QSVM;
import Training.Filters.TrainingFilter;
import Training.Model.TrainingTask;
import com.mathworks.toolbox.javabuilder.MWNumericArray;

import java.util.List;

/**
 * Created by alan on 10/24/16.
 */
public class ClassificationSVM extends TrainingMethod implements TrainingFilter {
    public int handleTraining(TrainingTask task) {
        return super.handleTraining(task);
    }

    @Override
    protected void doTraining(List<List<Double>> X_Matrix, List<Integer> Y_Matrix) {
        super.doTraining(X_Matrix, Y_Matrix);

        MWNumericArray output = null;
        Object[] result = null;
        try {
            SVM csvm = null;
            csvm= new SVM();
            parameters.put("csvm", csvm);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}