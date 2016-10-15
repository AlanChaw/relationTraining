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
public class LogisticRegression implements TrainingFilter {

    private TrainingTask trainingTask;
    private double[] Theta;

    public int handleTraining(TrainingTask task) {
        this.trainingTask = task;

        int Xcount = trainingTask.getEntityPairs().get(0).getPointWordExtendList().size();

        List<List<Double>> X_Matrix = new ArrayList<List<Double>>();
        List<Integer> Y_Matrix = new ArrayList<Integer>();
        for (EntityPairExtend entityPair : trainingTask.getEntityPairs()){
            List<Double> X = extractWeightings(entityPair);
            Integer Y = entityPair.getEntityPair().getRelation();
            X_Matrix.add(X);
            Y_Matrix.add(Y);
        }

        doTraining(X_Matrix, Y_Matrix);
        parameters.put("theta", this.Theta);

        return 0;
    }

    private List<Double> extractWeightings(EntityPairExtend entityPair){
        List<Double> weightings = new ArrayList<Double>();
        for (PointWordExtend pointWord : entityPair.getPointWordExtendList()){
            weightings.add(pointWord.getStatisticValue());
        }
        return weightings;
    }

    /**
     * 训练方法, 输入X矩阵, Y矩阵, 输出训练结果theta
     * @param X_Matrix
     * @param Y_Matrix
     */
    private void doTraining(List<List<Double>> X_Matrix, List<Integer> Y_Matrix) {
        try{
            File writename = new File("./file/middleFile.txt"); // 相对路径
            writename.createNewFile(); // 创建新文件
            BufferedWriter out = new BufferedWriter(new FileWriter(writename));
            System.out.println("X大小:" + X_Matrix.size() + "    " + X_Matrix.get(0).size());
            for (int i = 0; i < X_Matrix.size(); i++){
                for (int j = 0; j < X_Matrix.get(0).size(); j++){
                    out.write(X_Matrix.get(i).get(j).toString() + " ");
                }
                out.write(Y_Matrix.get(i).toString() + "\r\n");
            }
            out.flush();
            out.close();
        }catch (Exception e){
            e.printStackTrace();
        }

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

    }

    public void setTheta(double[] theta) {
        Theta = theta;
    }

    public double[] getTheta() {
        return Theta;
    }
}
