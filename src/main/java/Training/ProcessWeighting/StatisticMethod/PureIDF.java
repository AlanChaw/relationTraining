package Training.ProcessWeighting.StatisticMethod;

import DealFile.Model.Doc;
import DealFile.Model.EntityPair;
import DealFile.Model.Lemma;
import DealFile.Model.OriginFile;
import Training.Filters.WeightingFilter;
import Training.Model.*;
import Training.ProcessWeighting.HelpMethods;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by alan on 16/9/10.
 */
public class PureIDF implements WeightingFilter {

    private Integer D = 0;
//    private Integer Dw = 0;

    protected WeightingTask weightingTask;

    public int handleWeighting(WeightingTask task) {
        this.weightingTask = task;
        List<MatchSentence> allSentences = new ArrayList<MatchSentence>();

        List<EntityPair> allEntityPairs = new ArrayList<EntityPair>();
        allEntityPairs.addAll(this.weightingTask.getTrainingSetCompete());
        allEntityPairs.addAll(this.weightingTask.getTrainingSetCooperate());

        List<PointWordExtend> allPointWords = new ArrayList<PointWordExtend>();
        allPointWords.addAll(weightingTask.getPointWordExtendListCompete());
        allPointWords.addAll(weightingTask.getPointWordExtendListCooperate());

        for (EntityPair entityPair : allEntityPairs){
            Integer identifi = Integer.valueOf(entityPair.getIdentifi());
            OriginFile originFile = HelpMethods.fileJsonToModel(weightingTask.getOriginFileList().get(identifi));
            for (Doc doc : originFile.getDocs()){
                List<MatchSentence> sentences = HelpMethods.findSentencesInDoc(doc, entityPair);
                allSentences.addAll(sentences);
                D += sentences.size();
            }
        }

        System.out.println("IDF, 总句子数 : " + D);

        for (MatchSentence matchSentence : allSentences){
            doTheTraining(matchSentence, allPointWords);
        }

        doStatistics(allPointWords);

        return 0;
    }

    private void doTheTraining(MatchSentence matchSentence, List<PointWordExtend> pointWordExtendList){
        for (Lemma lemma : matchSentence.getLemmas()){        List<PointWordExtend> allPointWords = new ArrayList<PointWordExtend>();
            for (PointWordExtend pointWordExtend : pointWordExtendList){
                if (lemma.getLemma().equals(pointWordExtend.getPointWord().getLemma())){
                    pointWordExtend.setDw(pointWordExtend.getDw() + 1);
                    return;
                }
            }
        }


    }

    private void doStatistics(List<PointWordExtend> allPointWords){
        for (PointWordExtend pointWordExtend : allPointWords){
            if (pointWordExtend.getDw() == 0){
                pointWordExtend.setInverseDocumentFrequency(0.0);
                pointWordExtend.setStatisticValue(0.0);
                continue;
            }

            Double idf = (double)D / pointWordExtend.getDw();
            idf = Math.log(idf);
            pointWordExtend.setInverseDocumentFrequency(idf);
            //把统计值直接设为idf
            pointWordExtend.setStatisticValue(idf);

            System.out.println(pointWordExtend.toString());
        }


    }



}