package Training;

import DealFile.*;
import DealFile.Model.EntityPair;
import PointerWord.CompeteClosure;
import PointerWord.CooperateClosure;
import PointerWord.PointWord;
import Training.Filters.PredictFilter;
import Training.Filters.TrainingFilter;
import Training.Model.EntityPairExtend;
import Training.Model.PointWordExtend;
import Training.Model.PredictTask;
import Training.Model.TrainingTask;
import Training.ProcessPredict.PureTFPredict;
import Training.ProcessTraining.PureIDF;
import Training.ProcessTraining.PureTF;
import Training.ProcessTraining.PureTF2;
import Training.ProcessTraining.TF_IDF;
import net.sf.extjwnl.JWNLException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by alan on 16/7/18.
 */
public class Entry {

    //测试集大小, 这里定义竞争和合作关系各取25个为测试集
    private static final int TESTSETNUM = 25;

    private List<JSONObject> originFileList;
    private List<EntityPair> entityPairList;

    private List<PointWord> competePointWords;
    private List<PointWord> cooperatePointWords;
    private List<PointWordExtend> competeExtendedPonintWords;
    private List<PointWordExtend> cooperateExtendedPointWords;

    private List<EntityPair> competeList;
    private List<EntityPair> cooperateList;
    private List<EntityPair> trainingSetCompete;
    private List<EntityPair> trainingSetCooperate;
    private List<EntityPair> testSetCompete;
    private List<EntityPair> testSetCooperate;

    public Entry() throws java.io.IOException, JWNLException{

        System.out.println("处理文档");
        this.originFileList = new ArrayList<JSONObject>();
        this.originFileList = DealOriginFile.getInstance().getOriginFileList();
        this.entityPairList = new ArrayList<EntityPair>();
        this.entityPairList = DealIndexFile.getInstance().getEntityPairList();
        System.out.println("处理文档 ok");
        System.out.println("------------------");

        System.out.println("生成指示词闭包");
        this.competePointWords = new ArrayList<PointWord>();
        this.competePointWords = CompeteClosure.getInstance().getPointWordList();
        this.cooperatePointWords = new ArrayList<PointWord>();
        this.cooperatePointWords = CooperateClosure.getInstance().getPointWordList();
        System.out.println("指示词闭包 ok");
        System.out.println("------------------");

        this.competeList = new ArrayList<EntityPair>();
        this.cooperateList = new ArrayList<EntityPair>();
        this.competeExtendedPonintWords = new ArrayList<PointWordExtend>();
        this.cooperateExtendedPointWords = new ArrayList<PointWordExtend>();
        this.testSetCompete = new ArrayList<EntityPair>();
        this.testSetCooperate = new ArrayList<EntityPair>();
        this.trainingSetCompete = new ArrayList<EntityPair>();
        this.trainingSetCooperate = new ArrayList<EntityPair>();

        for (PointWord pointWord : competePointWords){
            PointWordExtend pointWordExtend = new PointWordExtend(pointWord);
            this.competeExtendedPonintWords.add(pointWordExtend);
        }
        for (PointWord pointWord : cooperatePointWords){
            PointWordExtend pointWordExtend = new PointWordExtend(pointWord);
            this.cooperateExtendedPointWords.add(pointWordExtend);
        }

        //生成训练集和测试集
//        generateTrainingAndTestSet_test();
        generateTrainingAndTestSet();

        //对每个训练集关系对进行训练
        trainEveryEntityPair();
        //用训练结果进行预测
        PredictTask predictTask = doPredict();
        //对预测结果进行评估
        doEstimate(predictTask);

    }
//
    /**
     * 随机生成测试集和训练集
     * 取1/10为测试集,并保证测试集中竞争和合作关系对的数量相等
     */
    private void generateTrainingAndTestSet(){
        Random rand = new Random();
//        for (int i = 0; i < 100; i++){
//
//            int randomNum = rand.nextInt(503);
//            System.out.println(randomNum);
//
//        }
        for (EntityPair entity : entityPairList){
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
    private boolean alreadyExist(List<EntityPair> entityPairList, Integer index, List<EntityPair> mainList){
        for (EntityPair entity : entityPairList){
            if (entity.getIdentifi().equals(mainList.get(index).getIdentifi()))
                return true;
        }

        return false;
    }

    /**
     * ##测试用##  固定取竞争和合作关系对集合中前25个关系对为测试集 其余为训练集
     */
    private void generateTrainingAndTestSet_test(){
        for (EntityPair entity : entityPairList){
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

        for (int i = 0; i < TESTSETNUM; i++){
            this.testSetCompete.add(competeList.get(i));
            this.testSetCooperate.add(cooperateList.get(i));
        }
        for (int i = TESTSETNUM; i < competeList.size(); i++){
            this.trainingSetCompete.add(competeList.get(i));
        }
        for (int i = TESTSETNUM; i < cooperateList.size(); i++){
            this.trainingSetCooperate.add(cooperateList.get(i));
        }

        System.out.println("竞争测试集大小:" + testSetCompete.size());
        System.out.println("合作测试集大小:" + testSetCooperate.size());
        System.out.println("竞争训练集大小:" + trainingSetCompete.size());
        System.out.println("合作训练集大小:" + trainingSetCooperate.size());
        System.out.println("------------------");
    }


    /**
     * 分别对两个训练集关系对中的每个关系对进行训练
     */
    private void trainEveryEntityPair(){

//        TrainingFilter filter = new PureTF();
//        TrainingFilter filter = new PureTF2();
        TrainingFilter filter = new PureIDF();
//        TrainingFilter filter = new TF_IDF();

        TrainingTask trainingTask = new TrainingTask();
        trainingTask.setOriginFileList(originFileList);

        trainingTask.setPointWordExtendListCompete(competeExtendedPonintWords);
        trainingTask.setTrainingSetCompete(trainingSetCompete);
        trainingTask.setPointWordExtendListCooperate(cooperateExtendedPointWords);
        trainingTask.setTrainingSetCooperate(trainingSetCooperate);

        filter.handleTraining(trainingTask);

    }

    private PredictTask doPredict(){
        List<EntityPairExtend> entityPairsToPredict = new ArrayList<EntityPairExtend>();
        for (int i = 0; i < testSetCompete.size() + testSetCooperate.size(); i++){
            EntityPairExtend entityPairExtend = new EntityPairExtend();
            if (i < testSetCompete.size()){
                entityPairExtend.setEntityPair(testSetCompete.get(i));
            }else {
                entityPairExtend.setEntityPair(testSetCooperate.get(i - testSetCompete.size()));
            }
            entityPairExtend.setPredictValue(0);
            entityPairsToPredict.add(entityPairExtend);
        }

        PredictFilter fliter = new PureTFPredict();
        PredictTask predictTask = new PredictTask();
        predictTask.setOriginFileList(originFileList);
        predictTask.setCompeteExtendedPointWords(competeExtendedPonintWords);
        predictTask.setCooperateExtendedPointWords(cooperateExtendedPointWords);
        predictTask.setEntityPairsToPredict(entityPairsToPredict);

        fliter.handlePredict(predictTask);

        return predictTask;
    }

    private void doEstimate(PredictTask predictTask){
        //计算准确率
        Double accuracy = caculateAccuracy(predictTask);
        //计算精确率(合作)
        Double precisionCooperate = caculatePrecisionCooperate(predictTask);
        //计算召回率(合作)
        Double recallCooperate = caculateRecallCooperate(predictTask);
        //计算F1值(合作)
        Double FOneValueCooperate = caculateFOne(precisionCooperate, recallCooperate);


        Double precisionCompete = caculatePrecisionCompete(predictTask);
        Double recallCompete = caculateRecallCompete(predictTask);
        Double FOneValueCompete = caculateFOne(precisionCompete, recallCompete);

//        System.out.println("准确率: " + accuracy);
//        System.out.println("合作精确率: " + precisionCooperate);
//        System.out.println("合作召回率: " + recallCooperate);
//        System.out.println("合作F1值: " + FOneValueCooperate);
//        System.out.println("竞争精确率: " + precisionCompete);
//        System.out.println("竞争召回率: " + recallCompete);
//        System.out.println("竞争F1值: " + FOneValueCompete);
        System.out.println(accuracy);
        System.out.println(precisionCooperate);
        System.out.println(recallCooperate);
        System.out.println(FOneValueCooperate);
        System.out.println(precisionCompete);
        System.out.println(recallCompete);
        System.out.println(FOneValueCompete);
    }

    private Double caculateAccuracy(PredictTask predictTask){
        Integer allNum = 0;
        Integer correctNum = 0;

        for (EntityPairExtend entityPairExtend : predictTask.getEntityPairsToPredict()){
            allNum++;

            if (entityPairExtend.getPredictValue() == entityPairExtend.getEntityPair().getRelation()){
                correctNum++;
            }
        }

        return (double)correctNum / allNum;
    }

    private Double caculatePrecisionCooperate(PredictTask predictTask){
        Integer cooperateNumPredict = 0;
        Integer cooperateNum = 0;
        for (EntityPairExtend entityPairExtend : predictTask.getEntityPairsToPredict()){
            if (entityPairExtend.getPredictValue() == 1){
                cooperateNumPredict++;
                if (entityPairExtend.getEntityPair().getRelation() == 1){
                    cooperateNum++;
                }
            }

        }

        //真实值为合作的关系对占预测为合作的关系对的比例
        return (double)cooperateNum / cooperateNumPredict;
    }

    private Double caculateRecallCooperate(PredictTask predictTask){
        Integer cooperateNum = 0;
        for (EntityPairExtend entityPairExtend : predictTask.getEntityPairsToPredict()){
            if (entityPairExtend.getPredictValue() == 1 && entityPairExtend.getEntityPair().getRelation() == 1){
                cooperateNum++;
            }
        }

        //预测为合作且正确的关系对占真实值为合作的关系对的比例
        return (double)cooperateNum / TESTSETNUM;
    }

    private Double caculatePrecisionCompete(PredictTask predictTask){
        Integer competeNumPredict = 0;
        Integer competeNum = 0;
        for (EntityPairExtend entityPairExtend : predictTask.getEntityPairsToPredict()){
            if (entityPairExtend.getPredictValue() == -1){
                competeNumPredict++;
                if (entityPairExtend.getEntityPair().getRelation() == -1){
                    competeNum++;
                }
            }

        }
        return (double)competeNum / competeNumPredict;

    }

    private Double caculateRecallCompete(PredictTask predictTask){
        Integer competeNum = 0;
        for (EntityPairExtend entityPairExtend : predictTask.getEntityPairsToPredict()){
            if (entityPairExtend.getPredictValue() == -1 && entityPairExtend.getEntityPair().getRelation() == -1){
                competeNum++;
            }
        }
        return (double)competeNum / TESTSETNUM;
    }

    private Double caculateFOne(Double precision, Double recall){
        return (2 * precision * recall) / (precision + recall);
    }


}
