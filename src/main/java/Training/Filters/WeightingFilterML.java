package Training.Filters;

import Training.Model.EntityPairExtend;
import Training.Model.WeightingTask;

import java.util.List;

/**
 * Created by alan on 16/10/13.
 */
public interface WeightingFilterML {

    List<EntityPairExtend> handleWeighting(WeightingTask task);

}
