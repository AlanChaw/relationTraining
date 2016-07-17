package Controller;

import PointerWord.CooperateClosure;
import net.sf.extjwnl.JWNLException;

/**
 * Created by alan on 16/7/16.
 */
public class MainController {

    public static void main(String[] args) throws JWNLException{

        CooperateClosure cooperateClosure = new CooperateClosure();
//        cooperateClosure.printAllTrees();
//        cooperateClosure.buildClosure();
        cooperateClosure.printWordList();
    }

}
