package Training.ProcessTraining;

import CSVM.SVM;
import KNNPredict.KNN;
import Training.Model.TrainingTask;
import com.mathworks.toolbox.javabuilder.MWNumericArray;

import java.util.List;

/**
 * Created by alan on 10/26/16.
 */
public class MLKNN extends TrainingMethod {

    public int handleTraining(TrainingTask task) {
        return super.handleTraining(task);
    }

    @Override
    protected void doTraining(List<List<Double>> X_Matrix, List<Integer> Y_Matrix) {
        super.doTraining(X_Matrix, Y_Matrix);

        MWNumericArray output = null;
        Object[] result = null;
        try {
            KNN knn = null;
            knn= new KNN();
            parameters.put("knn", knn);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
