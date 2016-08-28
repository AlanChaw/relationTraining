package Training;

import DealFile.*;
import PointerWord.CompeteClosure;
import PointerWord.CooperateClosure;
import PointerWord.PointWord;
import Training.ProcessPredict.DirectPredict;
import Training.ProcessTraining.DirectSearchTraining;
import net.sf.extjwnl.JWNLException;
import org.json.JSONArray;
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
        generateTrainingAndTestSet_test();
//        generateTrainingAndTestSet();

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

        TrainingFliter fliter = new DirectSearchTraining();
        TrainingTask trainingTask = new TrainingTask();
        trainingTask.setOriginFileList(originFileList);
        trainingTask.setPointWordExtendList(competeExtendedPonintWords);
        trainingTask.setTrainingSet(trainingSetCompete);
        fliter.handleTraining(trainingTask);

        trainingTask.setPointWordExtendList(cooperateExtendedPointWords);
        trainingTask.setTrainingSet(trainingSetCooperate);
        fliter.handleTraining(trainingTask);

    }

    private PredictTask doPredict(){
        List<EntityPairExtend> entityPairsToPredict = new ArrayList<EntityPairExtend>();
        for (int i = 0; i < trainingSetCompete.size() + trainingSetCooperate.size(); i++){
            EntityPairExtend entityPairExtend = new EntityPairExtend();
            if (i < trainingSetCompete.size()){
                entityPairExtend.setEntityPair(trainingSetCompete.get(i));
            }else {
                entityPairExtend.setEntityPair(trainingSetCooperate.get(i - trainingSetCompete.size()));
            }
            entityPairExtend.setPredictValue(0);
            entityPairsToPredict.add(entityPairExtend);
        }

        PredictFliter fliter = new DirectPredict();
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
        //计算精确率
        Double precision = caculatePrecision(predictTask);
        //计算召回率
        Double recall = caculateRecall(predictTask);

        System.out.println("准确率: " + accuracy);
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

    private Double caculatePrecision(PredictTask predictTask){


        return 0.0;
    }

    private Double caculateRecall(PredictTask predictTask){


        return 0.0;
    }

    public static OriginFile fileJsonToModel(JSONObject object){
        OriginFile fileModel = new OriginFile();
        String identifi = object.getString("identifi");

        List<Doc> docs = new ArrayList<Doc>();
        JSONArray docsArray = object.getJSONArray("docs");
        for (int i = 0; i < docsArray.length(); i++){
            JSONObject docJson = docsArray.getJSONObject(i);
            Doc doc = new Doc();
            doc.setContent(docJson.getString("content"));
            doc.setDocNum(docJson.getString("docNum"));
            doc.buildLemmaList();

            docs.add(doc);
        }

        fileModel.setDocs(docs);
        fileModel.setIdentifi(identifi);

        System.out.println("转换文档 " + identifi);

        return fileModel;
    }


}
