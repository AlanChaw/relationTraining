package Training.ProcessTraining;

import Training.Filters.TrainingFilter;
import Training.Model.EntityPairExtend;
import Training.Model.PointWordExtend;
import Training.Model.TrainingTask;

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


    }


}
