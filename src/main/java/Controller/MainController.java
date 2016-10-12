package Controller;

import DealFile.DealOriginFile;
import PointerWord.PointWordClosure;
import Training.Model.PointWordExtend;
import Training.ProcessWeighting.PureTF;
import Training.Entry;
import net.sf.extjwnl.JWNLException;

/**
 * Created by alan on 16/7/16.
 */
public class MainController {

    public static void main(String[] args) throws JWNLException, java.io.IOException{

        System.out.println("build ok");

        //最大文档编号
        DealOriginFile.DOCNUM = 500;
        //窗口长度
        PureTF.WINDOWLENGTH = 200;
        //近义词树的深度
        PointWordClosure.DEPTH = 2;
        //递减因子
        PointWordExtend.DECREASEFACTOR = 0.5;
//
//        System.out.println("关系对总数 " + (DealOriginFile.DOCNUM));
//        System.out.println("窗口长度 " + PureTF.WINDOWLENGTH);
//        System.out.println("wordne近义词树深度 " + PointWordClosure.DEPTH);
        System.out.println("------------------");

        Entry entry = new Entry();
    }

}
