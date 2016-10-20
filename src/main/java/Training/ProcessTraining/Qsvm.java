package Training.ProcessTraining;

import QuadraticSVM.QSVM;
import SLR.SimpleLogisticRegression;
import Training.Filters.TrainingFilter;
import Training.Model.TrainingTask;
import com.mathworks.extern.java.MWArray;
import com.mathworks.toolbox.javabuilder.MWNumericArray;

import java.util.List;

/**
 * Created by alan on 10/19/16.
 */
public class Qsvm extends TrainingMethod implements TrainingFilter {
    public int handleTraining(TrainingTask task) {
        return super.handleTraining(task);
    }


    @Override
    protected void doTraining(List<List<Double>> X_Matrix, List<Integer> Y_Matrix) {
        super.doTraining(X_Matrix, Y_Matrix);

        MWNumericArray output = null;
        Object[] result = null;
        try {
            QSVM qsvm = null;
            qsvm = new QSVM();
            parameters.put("qsvm", qsvm);
//            result = qsvm.QuadraticSVM(2);
//            Object predictFunc = result[0];
//            parameters.put("qsvm", qsv);
//            System.out.println("QSVM accuracy:" + result[1]);
//            System.out.print("ok");
        } catch (Exception e) {
            e.printStackTrace();
        }

//        parameters.put("theta", this.Theta);

    }

}
