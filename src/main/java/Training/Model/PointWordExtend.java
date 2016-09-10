package Training.Model;

import PointerWord.PointWord;

/**
 * Created by alan on 16/7/19.
 */


public class PointWordExtend {
    public static Double DECREASEFACTOR;

    private PointWord pointWord;
    private Double statisticValue;
    private Integer appearCount;

    public PointWordExtend(PointWord pointWord){
        this.pointWord = pointWord;
        this.statisticValue = 0.0;
        this.appearCount = 0;
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

    public Integer getAppearCount() {
        return appearCount;
    }

    public PointWord getPointWord() {
        return pointWord;
    }

    @Override
    public String toString() {
        return "word : " + pointWord + "\t" + "statisticValue : " + statisticValue + "\t" + "count : " + appearCount;
    }
}
