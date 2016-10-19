package Training.ProcessTraining;

import SLR.SimpleLogisticRegression;
import Training.Filters.TrainingFilter;
import Training.Model.EntityPairExtend;
import Training.Model.PointWordExtend;
import Training.Model.TrainingTask;
import com.mathworks.toolbox.javabuilder.MWClassID;
import com.mathworks.toolbox.javabuilder.MWNumericArray;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by alan on 16/10/12.
 */
public class LogisticRegression extends TrainingMethod implements TrainingFilter {
    private double[] Theta;

    public int handleTraining(TrainingTask task) {
        return super.handleTraining(task);
    }

    /**
     * 训练方法, 输入X矩阵, Y矩阵, 输出训练结果theta
     * @param X_Matrix
     * @param Y_Matrix
     */
    @Override
    protected void doTraining(List<List<Double>> X_Matrix, List<Integer> Y_Matrix) {
        super.doTraining(X_Matrix, Y_Matrix);
        MWNumericArray output = null;
        Object[] result = null;
        try {
            SimpleLogisticRegression slr = null;
            slr = new SimpleLogisticRegression();
            result = slr.SimpleLG(1);
            output = (MWNumericArray)result[0];
            double[] res = output.getDoubleData();
            setTheta(res);
            System.out.print("ok");
        }catch (Exception e){
            e.printStackTrace();
        }

        parameters.put("theta", this.Theta);
    }

    public void setTheta(double[] theta) {
        Theta = theta;
    }

    public double[] getTheta() {
        return Theta;
    }
}
