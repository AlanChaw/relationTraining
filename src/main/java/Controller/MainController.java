package Controller;

import DealFile.DealOriginFile;
import Training.TrainingEntry;
import net.sf.extjwnl.JWNLException;

/**
 * Created by alan on 16/7/16.
 */
public class MainController {

    public static void main(String[] args) throws JWNLException, java.io.IOException{

        System.out.println("build ok");
        System.out.println("------------------");

        //最大文档编号
        DealOriginFile.DOCNUM = 55;
        //窗口长度
        TrainingEntry.WINDOWLENGTH = 50;


        TrainingEntry trainingEntry = new TrainingEntry();
    }

}
