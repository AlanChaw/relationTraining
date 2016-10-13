package Training.Model;

import java.util.List;

/**
 * Created by alan on 16/10/12.
 */


public class TrainingTask {
    private List<EntityPairExtend> entityPairs; //统计过指示词权值的关系对

    public void setEntityPairs(List<EntityPairExtend> entityPairs) {
        this.entityPairs = entityPairs;
    }

    public List<EntityPairExtend> getEntityPairs() {
        return entityPairs;
    }
}
