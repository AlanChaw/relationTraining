package Training.Model;

import DealFile.Model.EntityPair;
import PointerWord.PointWord;

import java.util.Dictionary;
import java.util.List;

/**
 * Created by alan on 16/8/10.
 */
public class EntityPairExtend {
    private EntityPair entityPair;
    private Integer predictValue = 0;   //预测结果, -1表示竞争,1表示合作,0表示预测值相同
    private List<PointWordExtend> pointWordExtendList;

    public void setEntityPair(EntityPair entityPair) {
        this.entityPair = entityPair;
    }

    public void setPredictValue(Integer predictValue) {
        this.predictValue = predictValue;
    }

    public void setPointWordExtendList(List<PointWordExtend> pointWordExtendList) {
        this.pointWordExtendList = pointWordExtendList;
    }

    public EntityPair getEntityPair() {
        return entityPair;
    }

    public Integer getPredictValue() {
        return predictValue;
    }

    public List<PointWordExtend> getPointWordExtendList() {
        return pointWordExtendList;
    }

    @Override
    public String toString(){
        if (predictValue == -1){
            return "关系对: " + entityPair.toString() + "预测值: " + "竞争";
        }
        if (predictValue == 1){
            return "关系对: " + entityPair.toString() + "预测值: " + "合作";
        }
        if (predictValue == 0){
            return "关系对: " + entityPair.toString() + "预测值: " + "0";
        }

        return null;
    }

}
