package Training.Filters;

import Training.Model.WeightingTask;

/**
 * Created by alan on 16/10/12.
 */
public interface TrainingFilter {

    int handleTraining(WeightingTask task);

}
