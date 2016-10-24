package Controller;

import DealFile.DealOriginFile;
import PointerWord.PointWord;
import PointerWord.PointWordClosure;
import SLR.SimpleLogisticRegression;
import Training.Model.PointWordExtend;
import Training.ProcessWeighting.HelpMethods;
import Training.ProcessWeighting.StatisticMethod.PureTF;
import Training.Entry;
import com.mathworks.toolbox.javabuilder.MWClassID;
import com.mathworks.toolbox.javabuilder.MWNumericArray;
import net.sf.extjwnl.JWNLException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by alan on 16/7/16.
 */
public class MainController {

    public static void main(String[] args) throws JWNLException, java.io.IOException{

        System.out.println("build ok");

        //最大文档编号
        DealOriginFile.DOCNUM = 499;
        //窗口长度
        HelpMethods.WINDOWLENGTH = Integer.valueOf(args[0]);
//        HelpMethods.WINDOWLENGTH = 200;
        //近义词树的深度
        PointWordClosure.DEPTH = Integer.valueOf(args[1]);
//        PointWordClosure.DEPTH = 2;
        //递减因子
        PointWordExtend.DECREASEFACTOR = Double.valueOf(args[2]);
//        PointWordExtend.DECREASEFACTOR = 0.5;
//
//        System.out.println("关系对总数 " + (DealOriginFile.DOCNUM));
//        System.out.println("窗口长度 " + PureTF.WINDOWLENGTH);
//        System.out.println("wordne近义词树深度 " + PointWordClosure.DEPTH);
        System.out.println("------------------");

        Entry entry = new Entry();

    }

}
