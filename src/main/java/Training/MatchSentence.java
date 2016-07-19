package Training;

import DealFile.Lemma;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by alan on 16/7/19.
 */
public class MatchSentence {

    List<Lemma> sentence;
    Integer relation;

    public MatchSentence(){
        this.sentence = new ArrayList<Lemma>();

    }

    public void addLemma(Lemma lemma){
        this.sentence.add(lemma);
    }

    public void setRelation(Integer relation) {
        this.relation = relation;
    }

    public void setSentence(List<Lemma> sentence) {
        this.sentence = sentence;
    }

    public Integer getRelation() {
        return relation;
    }

    public List<Lemma> getSentence() {
        return sentence;
    }
}
