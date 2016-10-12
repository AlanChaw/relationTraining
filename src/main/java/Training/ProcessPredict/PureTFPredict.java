package Training.ProcessPredict;

import DealFile.Model.Doc;
import DealFile.Model.Lemma;
import DealFile.Model.OriginFile;
import Training.Filters.PredictFilter;
import Training.Model.EntityPairExtend;
import Training.Model.PointWordExtend;
import Training.Model.PredictTask;
import Training.ProcessWeighting.HelpMethods;

import java.util.List;

/**
 * Created by alan on 16/8/10.
 */
public class PureTFPredict implements PredictFilter {

    PredictTask predictTask;

    public int handlePredict(PredictTask task) {
        this.predictTask = task;

        for (EntityPairExtend entityPairExtend : predictTask.getEntityPairsToPredict()){
            doPredict(entityPairExtend);
        }

        return 0;
    }

    private void doPredict(EntityPairExtend entityPairExtend){
        Integer identifi = Integer.valueOf(entityPairExtend.getEntityPair().getIdentifi());
        OriginFile originFile = HelpMethods.fileJsonToModel(predictTask.getOriginFileList().get(identifi));
        List<Doc> docs = originFile.getDocs();

        Double cooperateValue = 0.0;
        Double competeValue = 0.0;


        for (Doc doc : docs){
            for (Lemma docLemma : doc.getLemmaList()){
                for (PointWordExtend pointWordExtend : predictTask.getCompeteExtendedPointWords()){
                    if (pointWordExtend.getPointWord().getLemma().equals(docLemma.getLemma()))
                        competeValue += pointWordExtend.getStatisticValue();
                }
                for (PointWordExtend pointWordExtend : predictTask.getCooperateExtendedPointWords()){
                    if (pointWordExtend.getPointWord().getLemma().equals(docLemma.getLemma()))
                        cooperateValue += pointWordExtend.getStatisticValue();
                }
            }
        }

//        for (Doc doc : docs){
//            List<MatchSentence> sentences = HelpMethods.findSentencesInDoc(doc, entityPairExtend.getEntityPair());
//            for (MatchSentence sentence : sentences){
//                for (Lemma sentenceLemma : sentence.getLemmas()){
//                    for (PointWordExtend pointWordExtend : predictTask.getCompeteExtendedPointWords()){
//                        if (pointWordExtend.getPointWord().getLemma().equals(sentenceLemma.getLemma()))
//                            competeValue += pointWordExtend.getStatisticValue();
//                    }
//                    for (PointWordExtend pointWordExtend : predictTask.getCooperateExtendedPointWords()){
//                        if (pointWordExtend.getPointWord().getLemma().equals(sentenceLemma.getLemma()))
//                            cooperateValue += pointWordExtend.getStatisticValue();
//                    }
//
//                }
//            }
//        }

        if (competeValue == cooperateValue){
            entityPairExtend.setPredictValue(0);
        }else if (competeValue > cooperateValue){
            entityPairExtend.setPredictValue(-1);
        }else if (cooperateValue > competeValue){
            entityPairExtend.setPredictValue(1);
        }

        System.out.println(entityPairExtend.toString());
        System.out.println("竞争值: " + competeValue + "\t" + "合作值: " + cooperateValue + "\n");
    }


}
