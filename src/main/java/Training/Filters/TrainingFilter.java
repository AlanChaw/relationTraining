package Training.Filters;

import Training.Model.TrainingTask;

/**
 * Created by alan on 16/7/30.
 */
public interface TrainingFilter {
    int handleTraining(TrainingTask task);

}
