package PointerWord;

/**
 * Created by alan on 16/7/16.
 */


/**
 * 指示词类,存储指示词词元及指示词级别
 */
public class PointWord {

    private String lemma;
    private Integer level;

    public PointWord(String lemma, Integer level){
        this.lemma = lemma;
        this.level = level;

    }

    public void print(){
        for (int i = 0; i <= level; i++){
            System.out.print(" ");
        }
        System.out.println("Word Lemma:" + lemma + ", Word level:" + level);
    }

    public String getLemma() {
        return lemma;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLemma(String lemma) {
        this.lemma = lemma;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    @Override
    public String toString() {
        return "lemma: " + lemma + "\tlevel:" + level;
    }
}
