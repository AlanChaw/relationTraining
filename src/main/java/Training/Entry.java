package Training;

import DealFile.*;
import DealFile.Model.EntityPair;
import PointerWord.CompeteClosure;
import PointerWord.CooperateClosure;
import PointerWord.PointWord;
import Training.Filters.PredictFilter;
import Training.Filters.TrainingFilter;
import Training.Filters.WeightingFilterML;
import Training.Model.*;
import Training.ProcessPredict.WknnPredict;
import Training.ProcessTraining.LogisticRegression;
import Training.ProcessTraining.Wknn;
import Training.ProcessWeighting.MLMethod.*;
import net.sf.extjwnl.JWNLException;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.*;

/**
 * Created by alan on 16/7/18.
 */
public class Entry {

    //测试集大小, 这里定义竞争和合作关系各取25个为测试集
    private static final int SETNUM = 160;

    private List<JSONObject> originFileList;
    private List<EntityPair> entityPairList;

    private List<PointWord> competePointWords;
    private List<PointWord> cooperatePointWords;
    private List<PointWordExtend> competeExtendedPonintWords;
    private List<PointWordExtend> cooperateExtendedPointWords;

    private List<List<EntityPairExtend>> competeTrainingList;
    private List<List<EntityPairExtend>> cooperateTrainingList;

    private List<EntityPairExtend> entityPairsWithWeightingsCompete;
    private List<EntityPairExtend> entityPairsWithWeightingsCooperate;

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

        this.competeExtendedPonintWords = new ArrayList<PointWordExtend>();
        this.cooperateExtendedPointWords = new ArrayList<PointWordExtend>();
        this.competeTrainingList = new ArrayList<List<EntityPairExtend>>();
        this.cooperateTrainingList = new ArrayList<List<EntityPairExtend>>();
        this.entityPairsWithWeightingsCompete = new ArrayList<EntityPairExtend>();
        this.entityPairsWithWeightingsCooperate = new ArrayList<EntityPairExtend>();

        for (PointWord pointWord : competePointWords){
            PointWordExtend pointWordExtend = new PointWordExtend(pointWord);
            this.competeExtendedPonintWords.add(pointWordExtend);
        }
        for (PointWord pointWord : cooperatePointWords){
            PointWordExtend pointWordExtend = new PointWordExtend(pointWord);
            this.cooperateExtendedPointWords.add(pointWordExtend);
        }

        List<EntityPair> competeList = new ArrayList<EntityPair>();
        List<EntityPair> cooperateList = new ArrayList<EntityPair>();
        for (EntityPair entityPair : this.entityPairList) {
            if (entityPair.getRelation() > 0) {
                if (cooperateList.size() < SETNUM) {
                    cooperateList.add(entityPair);
                }
            } else {
                if (competeList.size() < SETNUM) {
                    competeList.add(entityPair);
                }
            }
        }

        //计算指示词的权重
        caculateWeightings(competeList,cooperateList);
        //生成训练集和测试集
        generateTrainingAndTestSet();
        //进行训练
        beginTraining();

    }
//

    /**
     * 生成测试集和训练集, 采用10折交叉验证的方法
     *
     */
    private void generateTrainingAndTestSet(){
        Random rand = new Random();

        List<Integer> numbersCompete = new ArrayList<Integer>();
        int i = 0;
        while (i < SETNUM){
            Integer number = rand.nextInt(160);
            if (numbersCompete.contains(number)){
                continue;
            }else {
                numbersCompete.add(number);
                i++;
            }
        }

        List<Integer> numbersCooperate = new ArrayList<Integer>();
        i = 0;
        while (i < SETNUM){
            Integer number = rand.nextInt(160);
            if (numbersCooperate.contains(number)){
                continue;
            }else {
                numbersCooperate.add(number);
                i++;
            }
        }

        for (int n = 0; n < 10; n++){
            List<EntityPairExtend> entityPairsFold = new ArrayList<EntityPairExtend>();
            for (int m = 0; m < SETNUM / 10; m++){
                int num = n * (SETNUM / 10) + m;
                int index = numbersCompete.get(num);
                entityPairsFold.add(this.entityPairsWithWeightingsCompete.get(index));
            }
            this.competeTrainingList.add(entityPairsFold);

            List<EntityPairExtend> entityPairsFold2 = new ArrayList<EntityPairExtend>();
            for (int m = 0; m < SETNUM / 10; m++){
                int num = n * (SETNUM / 10) + m;
                int index = numbersCooperate.get(num);
                entityPairsFold2.add(this.entityPairsWithWeightingsCooperate.get(index));
            }
            this.cooperateTrainingList.add(entityPairsFold2);
        }
    }

    /**
     * ##测试用##
     */
    private void generateTrainingAndTestSet_test(){
    }

    private void beginTraining(){
        List<HashMap<String, Double>> resultMapList = new ArrayList<HashMap<String, Double>>();
        for (int i = 0; i < 10; i++) {
            List<EntityPairExtend> trainingSet = new ArrayList<EntityPairExtend>();
            List<EntityPairExtend> testSet = new ArrayList<EntityPairExtend>();
            for (int j = 0; j < 10; j++){
                if (j == i)
                    continue;
                trainingSet.addAll(this.competeTrainingList.get(j));
                trainingSet.addAll(this.cooperateTrainingList.get(j));
            }
            testSet.addAll(this.cooperateTrainingList.get(i));
            testSet.addAll(this.competeTrainingList.get(i));

            //进行二分类训练
            HashMap<String, Object> parameters = doTheTrainingML(trainingSet);
            //用训练结果进行预测
            PredictTask predictTask = doPredict(parameters, testSet);
            //对预测结果进行评估
            System.out.println("第" + (i + 1) + "次训练结果:");
            HashMap<String, Double> resultMap = doEstimate(predictTask);
            resultMapList.add(resultMap);
        }


        try{
            File writename = new File("./file/resultEstimite.txt"); // 相对路径
            writename.createNewFile(); // 创建新文件
            BufferedWriter out = new BufferedWriter(new FileWriter(writename));

            Double accuracySum = 0.0;
            Double precisionCooperateSum = 0.0;
            Double recallCooperateSum = 0.0;
            Double FOneValueCooperateSum = 0.0;
            Double precisionCompeteSum = 0.0;
            Double recallCompeteSum = 0.0;
            Double FOneValueCompeteSum = 0.0;
            for (int i = 0; i < 10; i++){
                HashMap<String, Double> resultMap = resultMapList.get(i);
                out.write("第" + (i + 1) + "次训练" + "\r\n");
                out.write("准确率: " + resultMap.get("accuracy") + "\r\n");
                out.write("合作精确率: " + resultMap.get("precisionCooperate") + "\r\n");
                out.write("合作召回率: " + resultMap.get("recallCooperate") + "\r\n");
                out.write("合作F1值: " + resultMap.get("FOneValueCooperate") + "\r\n");
                out.write("竞争精确率: " + resultMap.get("precisionCompete") + "\r\n");
                out.write("竞争召回率: " + resultMap.get("recallCompete") + "\r\n");
                out.write("竞争F1值: " + resultMap.get("FOneValueCompete") + "\r\n\r\n\r\n");
                out.flush();

                accuracySum += resultMap.get("accuracy");
                precisionCooperateSum += resultMap.get("precisionCooperate");
                recallCooperateSum += resultMap.get("recallCooperate");
                FOneValueCooperateSum += resultMap.get("FOneValueCooperate");
                precisionCompeteSum += resultMap.get("precisionCompete");
                recallCompeteSum += resultMap.get("recallCompete");
                FOneValueCompeteSum += resultMap.get("FOneValueCompete");
            }

            out.write("10次随机交叉验证指标平均值: " + "\r\n");
            out.write("准确率: " + accuracySum / 10 + "\r\n");
            out.write("合作精确率: " + precisionCooperateSum / 10 + "\r\n");
            out.write("合作召回率: " + recallCooperateSum / 10 + "\r\n");
            out.write("合作F1值: " + FOneValueCooperateSum / 10 + "\r\n");
            out.write("竞争精确率: " + precisionCompeteSum / 10 + "\r\n");
            out.write("竞争召回率: " + recallCompeteSum / 10 + "\r\n");
            out.write("竞争F1值: " + FOneValueCompeteSum / 10 + "\r\n\r\n\r\n");
            out.flush();
            out.close();
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    /**
     * 分别对两个训练集关系对中的每个关系对进行训练
     */
    private void caculateWeightings(List<EntityPair> competeList, List<EntityPair> cooperateList){

//        WeightingFilter filter = new PureTF();
//        WeightingFilter filter = new PureTF2();
//        WeightingFilter filter = new PureIDF();
//        WeightingFilter filter = new TF_IDF();
//        WeightingFilter filter = new TF2_IDF();
//        WeightingFilter filter = new PureIWF();
//        WeightingFilter filter = new TF_IWF();

//        WeightingFilterML filter = new MLPureTF();
//        WeightingFilterML filter = new MLPureIDF();
        WeightingFilterML filter = new MLTFIDF();
//        WeightingFilterML filter = new MLPureIWF();
//        WeightingFilterML filter = new MLTFIWF();

        WeightingTask weightingTask = new WeightingTask();
        weightingTask.setOriginFileList(originFileList);

        weightingTask.setPointWordExtendListCompete(competeExtendedPonintWords);
        weightingTask.setEntityPairSetCompete(competeList);
        weightingTask.setPointWordExtendListCooperate(cooperateExtendedPointWords);
        weightingTask.setEntityPairSetCooperate(cooperateList);

        filter.handleWeighting(weightingTask);

        this.entityPairsWithWeightingsCompete.addAll(filter.competeEntityPairsWithWeightings);
        this.entityPairsWithWeightingsCooperate.addAll(filter.cooperateEntityPairsWithWeightings);
    }

    private void doTheTraining(List<EntityPairExtend> entityPairsWithWeightings){
        TrainingTask task = new TrainingTask();
        task.setEntityPairs(entityPairsWithWeightings);
        TrainingFilter filter = new LogisticRegression();
        filter.handleTraining(task);
    }

    private HashMap<String, Object> doTheTrainingML(List<EntityPairExtend> entityPairsWithWeightings){
        TrainingTask task = new TrainingTask();
        task.setEntityPairs(entityPairsWithWeightings);

//        TrainingFilter filter = new LogisticRegression();
//        TrainingFilter filter = new Qsvm();
        TrainingFilter filter = new Wknn();

        filter.handleTraining(task);
        HashMap<String, Object> MLParameters = filter.parameters;
        return MLParameters;
    }

    private PredictTask doPredict(HashMap<String, Object> parameters, List<EntityPairExtend> testSet){
//        PredictFilter fliter = new PureTFPredict();
//        PredictFilter predictFilter = new LogisticRegressionPredict();
//        PredictFilter predictFilter = new QsvmPredict();
        PredictFilter predictFilter = new WknnPredict();

        PredictTask predictTask = new PredictTask();
        predictTask.setOriginFileList(originFileList);
        predictTask.setCompeteExtendedPointWords(competeExtendedPonintWords);
        predictTask.setCooperateExtendedPointWords(cooperateExtendedPointWords);
        predictTask.setEntityPairsToPredict(testSet);
        predictTask.setParameters(parameters);
        predictFilter.handlePredict(predictTask);
        return predictTask;
    }

    private HashMap<String, Double> doEstimate(PredictTask predictTask){
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

        System.out.println("准确率: " + accuracy);
        System.out.println("合作精确率: " + precisionCooperate);
        System.out.println("合作召回率: " + recallCooperate);
        System.out.println("合作F1值: " + FOneValueCooperate);
        System.out.println("竞争精确率: " + precisionCompete);
        System.out.println("竞争召回率: " + recallCompete);
        System.out.println("竞争F1值: " + FOneValueCompete);

        HashMap<String, Double> resultMap = new HashMap<String, Double>();
        resultMap.put("accuracy", accuracy);
        resultMap.put("precisionCooperate", precisionCooperate);
        resultMap.put("recallCooperate", recallCooperate);
        resultMap.put("FOneValueCooperate", FOneValueCooperate);
        resultMap.put("precisionCompete", precisionCompete);
        resultMap.put("recallCompete", recallCompete);
        resultMap.put("FOneValueCompete", FOneValueCompete);
        return resultMap;

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
        return (double)cooperateNum / this.cooperateTrainingList.get(0).size();
    }

    private Double caculatePrecisionCompete(PredictTask predictTask){
        Integer competeNumPredict = 0;
        Integer competeNum = 0;
        for (EntityPairExtend entityPairExtend : predictTask.getEntityPairsToPredict()){
            if (entityPairExtend.getPredictValue() == 0){
                competeNumPredict++;
                if (entityPairExtend.getEntityPair().getRelation() == 0){
                    competeNum++;
                }
            }

        }
        return (double)competeNum / competeNumPredict;

    }

    private Double caculateRecallCompete(PredictTask predictTask){
        Integer competeNum = 0;
        for (EntityPairExtend entityPairExtend : predictTask.getEntityPairsToPredict()){
            if (entityPairExtend.getPredictValue() == 0 && entityPairExtend.getEntityPair().getRelation() == 0){
                competeNum++;
            }
        }
        return (double)competeNum / this.competeTrainingList.get(0).size();
    }

    private Double caculateFOne(Double precision, Double recall){
        return (2 * precision * recall) / (precision + recall);
    }


}
