package Training;

import DealFile.DealIndexFile;
import DealFile.DealOriginFile;
import DealFile.Entity;
import DealFile.OriginFile;
import PointerWord.CompeteClosure;
import PointerWord.CooperateClosure;
import PointerWord.PointWord;
import net.sf.extjwnl.JWNLException;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by alan on 16/7/18.
 */
public class TrainingEntry {

    private List<OriginFile> originFileList;
    private List<Entity> entityList;

    private List<PointWord> competePointWords;
    private List<PointWord> cooperatePointWords;

    private List<Entity> competeList;
    private List<Entity> cooperateList;
    private List<Entity> trainingSetCompete;
    private List<Entity> trainingSetCooperate;
    private List<Entity> testSetCompete;
    private List<Entity> testSetCooperate;

    public TrainingEntry() throws java.io.IOException, JWNLException{


        System.out.println("处理文档");
        this.originFileList = new ArrayList<OriginFile>();
        this.originFileList = DealOriginFile.getInstance().getOriginFileList();
        this.entityList = new ArrayList<Entity>();
        this.entityList = DealIndexFile.getInstance().getEntityList();
        System.out.println("------------------");

        System.out.println("生成指示词闭包");
        this.competePointWords = new ArrayList<PointWord>();
        this.competePointWords = CompeteClosure.getInstance().getPointWordList();
        this.cooperatePointWords = new ArrayList<PointWord>();
        this.cooperatePointWords = CooperateClosure.getInstance().getPointWordList();
        System.out.println("------------------");


        this.competeList = new ArrayList<Entity>();
        this.cooperateList = new ArrayList<Entity>();
        this.testSetCompete = new ArrayList<Entity>();
        this.testSetCooperate = new ArrayList<Entity>();
        this.trainingSetCompete = new ArrayList<Entity>();
        this.trainingSetCooperate = new ArrayList<Entity>();
        generateTrainingAndTestSet();
    }

    public void generateTrainingAndTestSet(){
        Random rand = new Random();
//        for (int i = 0; i < 100; i++){
//
//            int randomNum = rand.nextInt(503);
//            System.out.println(randomNum);
//
//        }
        for (Entity entity : entityList){
            if (entity.getRelation() > 0)
                cooperateList.add(entity);
            else
                competeList.add(entity);
        }

        System.out.println("合作关系对数量:" + cooperateList.size());
        System.out.println("竞争关系对数量:" + competeList.size());



    }


}
