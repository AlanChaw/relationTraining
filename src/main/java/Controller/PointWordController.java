package Controller;

import PointerWord.CompeteClosure;
import PointerWord.CooperateClosure;
import net.sf.extjwnl.JWNLException;

/**
 * Created by alan on 16/7/18.
 */
public class PointWordController {

    public PointWordController() throws JWNLException{
        System.out.println("处理指示词闭包");

        CompeteClosure competeClosure = CompeteClosure.getInstance();
        competeClosure.printWordList();

        System.out.println("------------------");

        CooperateClosure cooperateClosure = CooperateClosure.getInstance();
        cooperateClosure.printWordList();

        System.out.println("指示词闭包处理完毕");
    }

}
