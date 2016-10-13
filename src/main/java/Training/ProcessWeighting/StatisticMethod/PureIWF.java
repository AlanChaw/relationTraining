package Training.ProcessWeighting.StatisticMethod;

import DealFile.Model.Doc;
import DealFile.Model.EntityPair;
import DealFile.Model.Lemma;
import DealFile.Model.OriginFile;
import Training.Filters.WeightingFilter;
import Training.Model.MatchSentence;
import Training.Model.PointWordExtend;
import Training.Model.WeightingTask;
import Training.ProcessWeighting.HelpMethods;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by alan on 16/9/11.
 */
public class PureIWF implements WeightingFilter {

    private Integer wordNum;
    protected WeightingTask weightingTask;

    public int handleWeighting(WeightingTask task) {
        this.wordNum = 0;
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
            }
        }
        for (MatchSentence matchSentence : allSentences){
            this.wordNum += matchSentence.getLemmas().size();
        }


        System.out.println("IWF, 总句子数 : " + allSentences.size());
        System.out.println("IWF, 词语总数 : " + wordNum);


        for (MatchSentence sentence : allSentences){
            for (Lemma lemma : sentence.getLemmas()){
                for (PointWordExtend pointWordExtend : allPointWords){
                    if (pointWordExtend.getPointWord().getLemma().equals(lemma.getLemma())){
                        pointWordExtend.setAllAppearCount(pointWordExtend.getAllAppearCount() + 1);
                    }
                }
            }
        }

        for (PointWordExtend pointWordExtend : allPointWords){
            Double iwf = (double)wordNum / pointWordExtend.getAllAppearCount();
            iwf = Math.log(iwf);
            pointWordExtend.setInverseWordFrequency(iwf);
            pointWordExtend.setStatisticValue(iwf);
            if (pointWordExtend.getAllAppearCount() == 0){
                pointWordExtend.setInverseWordFrequency(0.0);
                pointWordExtend.setStatisticValue(0.0);
            }
            System.out.println("词汇 : " + pointWordExtend.getPointWord().getLemma() + "出现 " + pointWordExtend.getAllAppearCount() + "次  IWF值 : " + pointWordExtend.getInverseWordFrequency());
        }

        return 0;
    }
}
