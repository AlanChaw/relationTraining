package Controller;

import PointerWord.CompeteClosure;
import PointerWord.CooperateClosure;
import net.sf.extjwnl.JWNLException;

/**
 * Created by alan on 16/7/16.
 */
public class MainController {

    public static void main(String[] args) throws JWNLException{

        CompeteClosure.getInstance().printWordList();
        System.out.println("-----------------");
        CooperateClosure.getInstance().printWordList();
    }

}
