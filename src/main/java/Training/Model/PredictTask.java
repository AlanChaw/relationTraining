package Training.Model;

import Training.Model.EntityPairExtend;
import Training.Model.PointWordExtend;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

/**
 * Created by alan on 16/8/10.
 */
public class PredictTask {

    private List<JSONObject> originFileList;
    private List<EntityPairExtend> entityPairsToPredict;
    private List<PointWordExtend> competeExtendedPointWords;
    private List<PointWordExtend> cooperateExtendedPointWords;

    private HashMap<String, Object> parameters;
    public void setOriginFileList(List<JSONObject> originFileList) {
        this.originFileList = originFileList;
    }

    public void setEntityPairsToPredict(List<EntityPairExtend> entityPairsToPredict) {
        this.entityPairsToPredict = entityPairsToPredict;
    }

    public List<JSONObject> getOriginFileList() {
        return originFileList;
    }

    public List<EntityPairExtend> getEntityPairsToPredict() {
        return entityPairsToPredict;
    }

    public void setCompeteExtendedPointWords(List<PointWordExtend> competeExtendedPointWords) {
        this.competeExtendedPointWords = competeExtendedPointWords;
    }

    public void setCooperateExtendedPointWords(List<PointWordExtend> cooperateExtendedPointWords) {
        this.cooperateExtendedPointWords = cooperateExtendedPointWords;
    }

    public List<PointWordExtend> getCompeteExtendedPointWords() {
        return competeExtendedPointWords;
    }

    public List<PointWordExtend> getCooperateExtendedPointWords() {
        return cooperateExtendedPointWords;
    }

    public void setParameters(HashMap<String, Object> parameters) {
        this.parameters = parameters;
    }

    public HashMap<String, Object> getParameters() {
        return parameters;
    }
}
