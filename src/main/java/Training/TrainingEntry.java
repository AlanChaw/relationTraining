package Training;

import DealFile.*;
import PointerWord.CompeteClosure;
import PointerWord.CooperateClosure;
import PointerWord.PointWord;
import Training.ProcessTraining.DirectSearchTraining;
import net.sf.extjwnl.JWNLException;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by alan on 16/7/18.
 */
public class TrainingEntry {

    //训练集大小, 这里定义竞争和合作关系各取25个为训练集
    private static final int TESTSETNUM = 5;

    private List<OriginFile> originFileList;
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

    public TrainingEntry() throws java.io.IOException, JWNLException{

        System.out.println("处理文档");
        this.originFileList = new ArrayList<OriginFile>();
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


        generateTrainingAndTestSet_test();
//        generateTrainingAndTestSet();
        trainEveryEntityPair();
    }

    /**
     * 随机生成测试集和训练集
     * 取1/10为测试集,并保证测试集中竞争和合作关系对的数量相等
     */
    public void generateTrainingAndTestSet(){
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
    public void generateTrainingAndTestSet_test(){
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
    public void trainEveryEntityPair(){
//        System.out.println("开始对竞争关系对训练");
//        Integer competeSentencesNum = 0;
//        for (EntityPair entityPair : trainingSetCompete){
//            Integer identifi = Integer.valueOf(entityPair.getIdentifi());
//            OriginFile originFile = originFileList.get(identifi);
//            for (Doc doc : originFile.getDocs()){
//                List<MatchSentence> sentences = findSentencesInDocs(doc, entityPair);
//                competeSentencesNum += sentences.size();
//                doTheTraining(sentences, entityPair.getRelation());
//            }
//        }
//        System.out.println("------------------");
//
//
//        System.out.println("开始对合作关系对训练");
//        Integer cooperateSentencesNum = 0;
//        for (EntityPair entityPair : trainingSetCooperate){
//            Integer identifi = Integer.valueOf(entityPair.getIdentifi());
//            OriginFile originFile = originFileList.get(identifi);
//            for (Doc doc : originFile.getDocs()){
//                List<MatchSentence> sentences = findSentencesInDocs(doc, entityPair);
//                cooperateSentencesNum+= sentences.size();
//                doTheTraining(sentences, entityPair.getRelation());
//            }
//        }
//        System.out.println("------------------");
//
//        doStatistic(competeSentencesNum, cooperateSentencesNum);
        TrainingFliter fliter = new DirectSearchTraining();
        Task task = new Task();
        task.setOriginFileList(originFileList);
        task.setPointWordExtendList(competeExtendedPonintWords);
        task.setTrainingSet(trainingSetCompete);
        fliter.handleTraining(task);

        task.setPointWordExtendList(cooperateExtendedPointWords);
        task.setTrainingSet(trainingSetCooperate);
        fliter.handleTraining(task);

    }

    /**********************核心部分*******************************/

    /**
     * 在某一关系对所出现的某一个文档中,找出在给定窗口长度下关系对中两个实体共同出现的句子
     * @param doc   给定的文档
     * @param entityPair    某一特定的实体对
     * @return 在该文档中所有匹配的句子
     */
//    public List<MatchSentence> findSentencesInDocs(Doc doc, EntityPair entityPair){
//        List<MatchSentence> sentences = new ArrayList<MatchSentence>();
//
//        //遍历整个文档,直接检索,找到实体词出现的位置
//        for (int i = 0; i < doc.getLemmaList().size(); i++){
//            Lemma lemma = doc.getLemmaList().get(i);
//            if (lemma.getLemma().equals(entityPair.getEntityName_1())){
//                //该词窗口的下界
//                Integer lowerBound = (i >= WINDOWLENGTH) ? (i - WINDOWLENGTH) : 0;
//                //该词窗口的上界(实际是下标,便于操作)
//                Integer upperBound = ((i + WINDOWLENGTH + 1) <= doc.getLemmaList().size()) ? (i + WINDOWLENGTH) : (doc.getLemmaList().size() - 1);
//
//                //设置句子
//                MatchSentence sentence = new MatchSentence();
//                sentence.setRelation(entityPair.getRelation());
//                for (int j = lowerBound; j <= upperBound; j++){
//                    sentence.addLemma(doc.getLemmaList().get(j));
//                }
//
//                //检索句子中是否有另一个实体词,如果有则保留
//                for (int j = 0; j < sentence.getLemmas().size(); j++){
//                    if (sentence.getLemmas().get(j).getLemma().equals(entityPair.getEntityName_2()))
//                        sentences.add(sentence);
//                }
//            }
//
//
//        }
//        return sentences;
//    }
//
//    /**
//     * 统计指示词在sentence中出现的次数
//     * @param sentences
//     * @param relation
//     */
//    public void doTheTraining(List<MatchSentence> sentences, Integer relation){
//        List<PointWordExtend> pointWordList = new ArrayList<PointWordExtend>();
//        if (relation < 0){
//            pointWordList = competeExtendedPonintWords;
//        }
//        else {
//            pointWordList = cooperateExtendedPointWords;
//        }
//
//
//        for (MatchSentence sentence : sentences){
//            for (Lemma lemma : sentence.getLemmas()){
//                for (PointWordExtend word : pointWordList){
//                    if (word.getPointWord().getLemma().equals(lemma.getLemma())){
//                        word.setAppearCount(word.getAppearCount() + 1);
////                        System.out.println("发现指示词: " + lemma.getLemma());
//                    }
//
//                }
//            }
//
//        }
//
//    }
//
//    /**
//     * 经过训练后,对每个指示词在句子中出现的频率统计,计算statisticValue
//     */
//    public void doStatistic(Integer competeSentencesNum, Integer cooperateSentencesNum){
//        System.out.println("竞争关系对中,两实体共同出现的句子: " + competeSentencesNum + "条");
//        for (PointWordExtend pointWordExtend : competeExtendedPonintWords){
//            if (pointWordExtend.getAppearCount() > 0){
//                System.out.println("词汇 " + pointWordExtend.getPointWord().getLemma() + " 出现: " + pointWordExtend.getAppearCount() + " 次");
//            }
//        }
//
//        System.out.println("合作关系中, 两实体共同出现的句子: " + cooperateSentencesNum + "条");
//        for (PointWordExtend pointWordExtend : cooperateExtendedPointWords){
//            if (pointWordExtend.getAppearCount() > 0){
//                System.out.println("词汇 " + pointWordExtend.getPointWord().getLemma() + " 出现" + pointWordExtend.getAppearCount() + "次");
//            }
//        }
//
//
//    }


}
