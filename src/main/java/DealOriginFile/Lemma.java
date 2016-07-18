package DealOriginFile;

/**
 * Created by alan on 16/7/18.
 */
public class Lemma {
    String lemma;
    String POS;
    String type;

    public Lemma(String wordString){

        //以"|"分割要加转义符
        String[] elements = wordString.split("\\|");

        this.lemma = elements[0];
        this.POS = elements[1];
        this.type = elements[2];

    }

    public String getLemma() {
        return lemma;
    }

    public String getPOS() {
        return POS;
    }

    public String getType() {
        return type;
    }

    public void setLemma(String lemma) {
        this.lemma = lemma;
    }

    public void setPOS(String POS) {
        this.POS = POS;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "[lemma: " + this.lemma + '\t' + "POS: " + this.POS + "\t" + "type:" + this.type + "]";
    }
}
