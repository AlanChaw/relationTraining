package Training;

import DealFile.EntityPair;
import DealFile.OriginFile;

import java.util.List;

/**
 * Created by alan on 16/7/30.
 */
public class Task {
    private List<EntityPair> trainingSet;
    private List<OriginFile> originFileList;
    private List<PointWordExtend> pointWordExtendList;

    public List<EntityPair> getTrainingSet() {
        return trainingSet;
    }

    public List<OriginFile> getOriginFileList() {
        return originFileList;
    }

    public List<PointWordExtend> getPointWordExtendList() {
        return pointWordExtendList;
    }

    public void setOriginFileList(List<OriginFile> originFileList) {
        this.originFileList = originFileList;
    }

    public void setPointWordExtendList(List<PointWordExtend> pointWordExtendList) {
        this.pointWordExtendList = pointWordExtendList;
    }

    public void setTrainingSet(List<EntityPair> trainingSet) {
        this.trainingSet = trainingSet;
    }
}
