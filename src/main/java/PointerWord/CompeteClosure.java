package PointerWord;

import net.sf.extjwnl.JWNLException;
import net.sf.extjwnl.data.IndexWord;
import net.sf.extjwnl.data.POS;

import java.util.ArrayList;

/**
 * Created by alan on 16/7/16.
 */


public class CompeteClosure extends PointWordClosure {
    private static CompeteClosure instance;

    private IndexWord COMPETE;
    private IndexWord CHALLENGE;
    private IndexWord AGAINST;      //against在wordnet中无法查找 要单独拿出来赋值
    private IndexWord VIE;
    private IndexWord CONTEND;
    private IndexWord FIGHT;
    private IndexWord CONTEST;
    private IndexWord DISPUTE;
    private IndexWord BATTLE;
    private IndexWord ACCUSE;

    public static synchronized CompeteClosure getInstance() throws JWNLException{
        if (instance == null) {
            instance = new CompeteClosure();

        }
        return instance;

    }

    private CompeteClosure() throws JWNLException{
        COMPETE = dictionary.lookupIndexWord(POS.VERB, "compete");
        CHALLENGE = dictionary.lookupIndexWord(POS.VERB, "challenge");
//        AGAINST = new IndexWord(dictionary, "against", POS.ADJECTIVE, null);
        VIE = dictionary.lookupIndexWord(POS.VERB, "vie");
        CONTEND = dictionary.lookupIndexWord(POS.VERB, "contend");
        FIGHT = dictionary.lookupIndexWord(POS.VERB, "fight");
        CONTEST = dictionary.lookupIndexWord(POS.VERB, "contest");
        DISPUTE = dictionary.lookupIndexWord(POS.VERB, "dispute");
        BATTLE = dictionary.lookupIndexWord(POS.VERB, "battle");
        ACCUSE = dictionary.lookupIndexWord(POS.VERB, "accuse");

        this.indexWordList = new ArrayList<IndexWord>();
        this.indexWordList.add(COMPETE);
        this.indexWordList.add(CHALLENGE);
        this.indexWordList.add(AGAINST);
        this.indexWordList.add(VIE);
        this.indexWordList.add(CONTEND);
        this.indexWordList.add(FIGHT);
        this.indexWordList.add(CONTEST);
        this.indexWordList.add(DISPUTE);
        this.indexWordList.add(BATTLE);
        this.indexWordList.add(ACCUSE);

        build();

        PointWord against = new PointWord("against", 0);
        this.pointWordList.add(against);
    }






}
