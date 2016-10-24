package Training.ProcessTraining;

import Training.Filters.TrainingFilter;
import Training.Model.EntityPairExtend;
import Training.Model.PointWordExtend;
import Training.Model.TrainingTask;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by alan on 10/19/16.
 */
public class TrainingMethod implements TrainingFilter {
    private TrainingTask trainingTask;

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
        return 0;
    }

    private List<Double> extractWeightings(EntityPairExtend entityPair){
        List<Double> weightings = new ArrayList<Double>();
        for (PointWordExtend pointWord : entityPair.getPointWordExtendList()){
            weightings.add(pointWord.getStatisticValue());
        }
        return weightings;
    }

    protected void doTraining(List<List<Double>> X_Matrix, List<Integer> Y_Matrix) {
        try{
            File writename = new File("/Users/alan/Documents/relationTraining/file/middleFile.txt");
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

    }


}
