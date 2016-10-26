package Training.Model;

import PointerWord.PointWord;

/**
 * Created by alan on 16/7/19.
 */


public class PointWordExtend implements Cloneable{
    public static Double DECREASEFACTOR;

    private PointWord pointWord;
    private Double statisticValue;   //即最后计算出的权值

    //TF
    private Double termFrequency;
    private Integer appearCount = 0;
    //IDF
    private Double inverseDocumentFrequency;
    private Integer Dw = 0;
    //IWF
    private Double inverseWordFrequency;
    private Integer allAppearCount = 0;
    //BDC
    private Integer Ccompete = 0;
    private Integer Ccooperate = 0;
    private Double weight = 0.0;
    private Integer appeared = 0;
    private Double weightInEntity = 0.0;

    public PointWordExtend(PointWord pointWord){
        this.pointWord = pointWord;
        this.statisticValue = 0.0;
        this.appearCount = 0;

        this.termFrequency = 0.0;
        this.inverseDocumentFrequency = 0.0;
    }

    public void setPointWord(PointWord pointWord) {
        this.pointWord = pointWord;
    }

    public void setAppearCount(Integer appearCount) {
        this.appearCount = appearCount;
    }

    public void setStatisticValue(Double statisticValue) {
        this.statisticValue = statisticValue * Math.pow(DECREASEFACTOR, this.pointWord.getLevel());
    }

    public Double getStatisticValue() {
        return statisticValue;
    }


    public void setTermFrequency(Double termFrequency) {
        this.termFrequency = termFrequency;
    }

    public Double getTermFrequency() {
        return termFrequency;
    }

    public void setInverseDocumentFrequency(Double inverseDocumentFrequency) {
        this.inverseDocumentFrequency = inverseDocumentFrequency;
    }

    public Double getInverseDocumentFrequency() {
        return inverseDocumentFrequency;
    }

    public Integer getAppearCount() {
        return appearCount;
    }

    public PointWord getPointWord() {
        return pointWord;
    }

    public void setDw(Integer dw) {
        Dw = dw;
    }

    public Integer getDw() {
        return Dw;
    }

    public void setAllAppearCount(Integer allAppearCount) {
        this.allAppearCount = allAppearCount;
    }

    public Integer getAllAppearCount() {
        return allAppearCount;
    }

    public void setInverseWordFrequency(Double inverseWordFrequency) {
        this.inverseWordFrequency = inverseWordFrequency;
    }

    public Double getInverseWordFrequency() {
        return inverseWordFrequency;
    }

    public void setCcompete(Integer ccompete) {
        Ccompete = ccompete;
    }

    public Integer getCcompete() {
        return Ccompete;
    }

    public void setCcooperate(Integer ccooperate) {
        Ccooperate = ccooperate;
    }

    public Integer getCcooperate() {
        return Ccooperate;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    public Double getWeight() {
        return weight;
    }

    public void setAppeared(Integer appeared) {
        this.appeared = appeared;
    }

    public Integer getAppeared() {
        return appeared;
    }

    public void setWeightInEntity(Double weightInEntity) {
        this.weightInEntity = weightInEntity;
    }

    public Double getWeightInEntity() {
        return weightInEntity;
    }

    @Override
    public String toString() {
        return "word : " + pointWord + "\t" + "statisticValue : " + statisticValue + "\t" + "count : " + Dw;
    }

    @Override
    public PointWordExtend clone() {
        PointWordExtend clone = null;
        try{
            clone = (PointWordExtend) super.clone();

        }catch(CloneNotSupportedException e){
            throw new RuntimeException(e);  // won't happen
        }

        return clone;
    }
}
