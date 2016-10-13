package Training.Model;

import DealFile.Model.EntityPair;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by alan on 16/7/30.
 */
public class WeightingTask {
    private List<JSONObject> originFileList;

    private List<EntityPair> trainingSetCompete;
    private List<PointWordExtend> pointWordExtendListCompete;

    private List<EntityPair> trainingSetCooperate;
    private List<PointWordExtend> pointWordExtendListCooperate;

    private List<EntityPairExtend> resultEntityPairs;


    public void setOriginFileList(List<JSONObject> originFileList) {
        this.originFileList = originFileList;
    }

    public List<JSONObject> getOriginFileList() {
        return originFileList;
    }

    public void setTrainingSetCompete(List<EntityPair> trainingSetCompete) {
        this.trainingSetCompete = trainingSetCompete;
    }

    public List<EntityPair> getTrainingSetCompete() {
        return trainingSetCompete;
    }

    public void setPointWordExtendListCompete(List<PointWordExtend> pointWordExtendListCompete) {
        this.pointWordExtendListCompete = pointWordExtendListCompete;
    }

    public List<PointWordExtend> getPointWordExtendListCompete() {
        return pointWordExtendListCompete;
    }

    public void setTrainingSetCooperate(List<EntityPair> trainingSetCooperate) {
        this.trainingSetCooperate = trainingSetCooperate;
    }

    public List<EntityPair> getTrainingSetCooperate() {
        return trainingSetCooperate;
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
