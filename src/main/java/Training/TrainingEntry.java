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
    //训练集大小, 这里定义竞争和合作关系各取25个为训练集
    private static final int TESTSETNUM = 25;

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

        Integer competeNum = competeList.size();
        Integer cooperateNum = cooperateList.size();

        System.out.println("竞争关系对数量:" + competeNum);
        System.out.println("合作关系对数量:" + cooperateNum);
        System.out.println("------------------");

        //生成表示竞争的测试集
        while (testSetCompete.size() < TESTSETNUM){
            Integer random = rand.nextInt(competeNum - 1);
            if (alreadyExist(testSetCompete, random, competeList))
                continue;
            testSetCompete.add(competeList.get(random));
        }

        //生成表示合作的测试集
        while (testSetCooperate.size() < TESTSETNUM){
            Integer random = rand.nextInt(cooperateNum - 1);
            if (alreadyExist(testSetCooperate, random, cooperateList))
                continue;
            else
                testSetCooperate.add(cooperateList.get(random));
        }

        //生成竞争关系训练集
        for (int i = 0; i < competeList.size(); i++){
            if (alreadyExist(testSetCompete, i, competeList))
                continue;
            trainingSetCompete.add(competeList.get(i));
        }

        //生成合作关系训练集
        for (int i = 0; i < cooperateList.size(); i++){
            if (alreadyExist(testSetCooperate, i, cooperateList))
                continue;
            trainingSetCooperate.add(cooperateList.get(i));
        }


        System.out.println("竞争测试集大小:" + testSetCompete.size());
        System.out.println("合作测试集大小:" + testSetCooperate.size());
        System.out.println("竞争训练集大小:" + trainingSetCompete.size());
        System.out.println("合作训练集大小:" + trainingSetCooperate.size());
        System.out.println("------------------");

    }

    //检测要添加进测试集的竞争实体是否已存在于测试集中
    private boolean alreadyExist(List<Entity> entityList, Integer index, List<Entity> mainList){
        for (Entity entity : entityList){
            if (entity.getIdentifi().equals(mainList.get(index).getIdentifi()))
                return true;
        }

        return false;
    }


//
//    //选择排序  FIX ME
//    private void sort(List<Entity> entityList){
//        List<Entity> sortedList = new ArrayList<Entity>(600);
//
//        for (int i = 0; i < entityList.size(); i++){
//            Entity min = entityList.get(i);
//            for (int j = i + 1; j < entityList.size(); j++){
//                if (Integer.valueOf(entityList.get(j).getIdentifi()) < Integer.valueOf(min.getIdentifi())){
//                    min = entityList.get(j);
//                }
//            }
//
//            //swap
//            Integer indexOfMin = entityList.indexOf(min);
//            Entity t = entityList.get(i);
//            entityList.set(i, min);
//            entityList.set(indexOfMin, t);
//        }
//
//
//    }


}
