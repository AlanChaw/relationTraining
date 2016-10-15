package Training.Model;

import DealFile.Model.EntityPair;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by alan on 16/7/30.
 */
public class WeightingTask {
    private List<JSONObject> originFileList;

    private List<EntityPair> entityPairSetCompete;
    private List<PointWordExtend> pointWordExtendListCompete;

    private List<EntityPair> entityPairSetCooperate;
    private List<PointWordExtend> pointWordExtendListCooperate;

    private List<EntityPairExtend> resultEntityPairs;


    public void setOriginFileList(List<JSONObject> originFileList) {
        this.originFileList = originFileList;
    }

    public List<JSONObject> getOriginFileList() {
        return originFileList;
    }

    public void setEntityPairSetCompete(List<EntityPair> entityPairSetCompete) {
        this.entityPairSetCompete = entityPairSetCompete;
    }

    public List<EntityPair> getEntityPairSetCompete() {
        return entityPairSetCompete;
    }

    public void setPointWordExtendListCompete(List<PointWordExtend> pointWordExtendListCompete) {
        this.pointWordExtendListCompete = pointWordExtendListCompete;
    }

    public List<PointWordExtend> getPointWordExtendListCompete() {
        return pointWordExtendListCompete;
    }

    public void setEntityPairSetCooperate(List<EntityPair> entityPairSetCooperate) {
        this.entityPairSetCooperate = entityPairSetCooperate;
    }

    public List<EntityPair> getEntityPairSetCooperate() {
        return entityPairSetCooperate;
    }

    public void setPointWordExtendListCooperate(List<PointWordExtend> pointWordExtendListCooperate) {
        this.pointWordExtendListCooperate = pointWordExtendListCooperate;
    }

    public List<PointWordExtend> getPointWordExtendListCooperate() {
        return pointWordExtendListCooperate;
    }

    public List<EntityPairExtend> getResultEntityPairs() {
        return resultEntityPairs;
    }

    public void setResultEntityPairs(List<EntityPairExtend> resultEntityPairs) {
        this.resultEntityPairs = resultEntityPairs;
    }
}
