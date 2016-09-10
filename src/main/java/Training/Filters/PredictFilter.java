package Training.Filters;

import Training.Model.PredictTask;

/**
 * Created by alan on 16/8/10.
 */
public interface PredictFilter {
    int handlePredict(PredictTask task);

}
