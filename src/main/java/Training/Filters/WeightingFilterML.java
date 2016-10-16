package Training.Filters;

import Training.Model.EntityPairExtend;
import Training.Model.WeightingTask;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by alan on 16/10/13.
 */
public interface WeightingFilterML {

    int handleWeighting(WeightingTask task);
    List<EntityPairExtend> competeEntityPairsWithWeightings = new ArrayList<EntityPairExtend>();
    List<EntityPairExtend> cooperateEntityPairsWithWeightings = new ArrayList<EntityPairExtend>();

}
