package Training;

import DealFile.Lemma;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by alan on 16/7/19.
 */
public class MatchSentence {

    List<Lemma> lemmas;
    Integer relation;

    public MatchSentence(){
        this.lemmas = new ArrayList<Lemma>();

    }

    public void addLemma(Lemma lemma){
        this.lemmas.add(lemma);
    }

    public void setRelation(Integer relation) {
        this.relation = relation;
    }

    public void setLemmas(List<Lemma> lemmas) {
        this.lemmas = lemmas;
    }

    public Integer getRelation() {
        return relation;
    }

    public List<Lemma> getLemmas() {
        return lemmas;
    }
}
