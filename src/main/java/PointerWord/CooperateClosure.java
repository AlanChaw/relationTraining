package PointerWord;

/**
 * Created by alan on 16/7/16.
 */

import net.sf.extjwnl.JWNLException;
import net.sf.extjwnl.data.IndexWord;
import net.sf.extjwnl.data.POS;
import net.sf.extjwnl.data.PointerUtils;
import net.sf.extjwnl.data.Word;
import net.sf.extjwnl.data.list.PointerTargetTree;
import net.sf.extjwnl.data.list.PointerTargetTreeNode;
import net.sf.extjwnl.dictionary.Dictionary;

import java.util.ArrayList;
import java.util.List;

/**
 * wbcao:合作指示词闭包
 */
public class CooperateClosure extends PointWordClosure{
    private static CooperateClosure instance;

    private IndexWord COOPERATE;
    private IndexWord ALLY;
    private IndexWord COLLABORATE;
    private IndexWord JOINT;
    private IndexWord OWN;
    private IndexWord PARTNER;
    private IndexWord COORDINATE;
    private IndexWord ENGAGE;
    private IndexWord PARTNERSHIP;
    private IndexWord AGREE;

    public static synchronized CooperateClosure getInstance() throws JWNLException{
        if (instance == null)
            instance = new CooperateClosure();
        return instance;
    }

    private CooperateClosure() throws JWNLException{

        COOPERATE = dictionary.lookupIndexWord(POS.VERB, "cooperate");
        ALLY = dictionary.lookupIndexWord(POS.VERB, "ally");
        COLLABORATE = dictionary.lookupIndexWord(POS.VERB, "collaborate");
        JOINT = dictionary.lookupIndexWord(POS.VERB, "joint");
        OWN = dictionary.lookupIndexWord(POS.VERB, "own");
        PARTNER = dictionary.lookupIndexWord(POS.NOUN, "partner");
        COORDINATE = dictionary.lookupIndexWord(POS.VERB, "coordinate");
        ENGAGE = dictionary.lookupIndexWord(POS.VERB, "engage");
        PARTNERSHIP = dictionary.lookupIndexWord(POS.NOUN, "partnership");
        AGREE = dictionary.lookupIndexWord(POS.VERB, "agree");

        this.indexWordList = new ArrayList<IndexWord>();
        this.indexWordList.add(COOPERATE);
        this.indexWordList.add(ALLY);
        this.indexWordList.add(COLLABORATE);
        this.indexWordList.add(JOINT);
        this.indexWordList.add(OWN);
        this.indexWordList.add(PARTNER);
        this.indexWordList.add(COORDINATE);
        this.indexWordList.add(ENGAGE);
        this.indexWordList.add(PARTNERSHIP);
        this.indexWordList.add(AGREE);

        build();

    }


}
