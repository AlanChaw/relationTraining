package Training;

import DealFile.EntityPair;
import DealFile.OriginFile;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by alan on 16/7/30.
 */
public class TrainingTask {
    private List<EntityPair> trainingSet;
    private List<JSONObject> originFileList;
    private List<PointWordExtend> pointWordExtendList;

    public List<EntityPair> getTrainingSet() {
        return trainingSet;
    }

    public List<JSONObject> getOriginFileList() {
        return originFileList;
    }

    public List<PointWordExtend> getPointWordExtendList() {
        return pointWordExtendList;
    }

    public void setOriginFileList(List<JSONObject> originFileList) {
        this.originFileList = originFileList;
    }

    public void setPointWordExtendList(List<PointWordExtend> pointWordExtendList) {
        this.pointWordExtendList = pointWordExtendList;
    }

    public void setTrainingSet(List<EntityPair> trainingSet) {
        this.trainingSet = trainingSet;
    }
}
