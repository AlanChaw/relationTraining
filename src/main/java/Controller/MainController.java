package Controller;

import DealOriginFile.*;
import PointerWord.CompeteClosure;
import PointerWord.CooperateClosure;
import net.sf.extjwnl.JWNLException;

import java.util.List;

/**
 * Created by alan on 16/7/16.
 */
public class MainController {

    public static void main(String[] args) throws JWNLException, java.io.IOException{

//        CompeteClosure.getInstance().printWordList();
//        System.out.println("-----------------");
//        CooperateClosure.getInstance().printWordList();

        PointWordController pointWordController = new PointWordController();
        OriginFileController originFileController = new OriginFileController();
    }

}
