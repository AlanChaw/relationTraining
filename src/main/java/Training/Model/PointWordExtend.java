package Training.Model;

import PointerWord.PointWord;

/**
 * Created by alan on 16/7/19.
 */


public class PointWordExtend {
    public static Double DECREASEFACTOR;

    private PointWord pointWord;
    private Double statisticValue;

    //TF
    private Double termFrequency;
    private Integer appearCount = 0;
    //IDF
    private Double inverseDocumentFrequency;
    private Integer Dw = 0;

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

    @Override
    public String toString() {
        return "word : " + pointWord + "\t" + "statisticValue : " + statisticValue + "\t" + "count : " + Dw;
    }
}
