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
import java.util.ArrayList;
import java.util.List;

/**
 * Created by alan on 16/10/12.
 */
public class LogisticRegression implements TrainingFilter {

    private TrainingTask trainingTask;
    private double[] Theta;

    public int handleTraining(TrainingTask task) {
        this.trainingTask = task;

        int rowCount = trainingTask.getEntityPairs().size();
        int columnCount = trainingTask.getEntityPairs().get(0).getPointWordExtendList().size();
        Double[][] X_Matrix = new Double[rowCount][columnCount];
        Double[][] Y_Matrix = new Double[rowCount][1];

        for (int i = 0; i < rowCount; i++){
            Double Y = (double) trainingTask.getEntityPairs().get(i).getEntityPair().getRelation();
            Double[] X = extractWeightings(trainingTask.getEntityPairs().get(i));
            X_Matrix[i] = X;
            Y_Matrix[i][0] = Y;
        }

        doTraining(X_Matrix, Y_Matrix);

        return 0;
    }

    private Double[] extractWeightings(EntityPairExtend entityPair){
        Double[] weightings = new Double[entityPair.getPointWordExtendList().size()];
        for (int i = 0; i < entityPair.getPointWordExtendList().size(); i++){
            weightings[i] = entityPair.getPointWordExtendList().get(i).getStatisticValue();
        }
        return weightings;
    }

    /**
     * 训练方法, 输入X矩阵, Y矩阵, 输出训练结果theta
     * @param X_Matrix
     * @param Y_Matrix
     */
    private void doTraining(Double[][] X_Matrix, Double[][] Y_Matrix) {
        MWNumericArray X = null;
        MWNumericArray y = null;
        MWNumericArray output = null;
        Object[] result = null;
        try {
            SimpleLogisticRegression slr = null;
            slr = new SimpleLogisticRegression();
            X = new MWNumericArray(X_Matrix, MWClassID.DOUBLE);
            y = new MWNumericArray(Y_Matrix, MWClassID.DOUBLE);
            int[] t1 = X.getDimensions();
            int[] t2 = y.getDimensions();
            result = slr.SimpleLG(1, X, y);
            output = (MWNumericArray)result[0];
            double[] res = output.getDoubleData();
            setTheta(res);
            System.out.print("ok");
        }catch (Exception e){
            e.printStackTrace();
        }

//        try{
//            File writename = new File("./file/middleFile.txt"); // 相对路径
//            writename.createNewFile(); // 创建新文件
//            BufferedWriter out = new BufferedWriter(new FileWriter(writename));
//            for (int i = 0; i < (X_Matrix.size() - 1); i++){
//                for (int j = 0; j < (X_Matrix.get(0).size() - 1); j++){
//                    out.write(X_Matrix.get(i).get(j).toString() + " ");
//                }
//                out.write(Y_Matrix.get(i).toString() + "\r\n");
//            }
//            out.flush();
//            out.close();
//        }catch (Exception e){
//            e.printStackTrace();
//        }

    }

    public void setTheta(double[] theta) {
        Theta = theta;
    }

    public double[] getTheta() {
        return Theta;
    }
}
