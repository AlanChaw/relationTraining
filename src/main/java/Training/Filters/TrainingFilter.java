package Training.Filters;

import Training.Model.TrainingTask;
import Training.Model.WeightingTask;

import java.util.HashMap;

/**
 * Created by alan on 16/10/12.
 */
public interface TrainingFilter {

    HashMap<String, Object> parameters = new HashMap<String, Object>();

    int handleTraining(TrainingTask task);

}
