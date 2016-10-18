package Training.ProcessWeighting.MLMethod;

import DealFile.Model.Doc;
import DealFile.Model.EntityPair;
import DealFile.Model.Lemma;
import DealFile.Model.OriginFile;
import Training.Filters.WeightingFilterML;
import Training.Model.EntityPairExtend;
import Training.Model.MatchSentence;
import Training.Model.PointWordExtend;
import Training.Model.WeightingTask;
import Training.ProcessWeighting.HelpMethods;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by alan on 16/10/17.
 */
public class MLPureIDF extends MLWeighting implements WeightingFilterML {

    public int handleWeighting(WeightingTask task) {
        return super.handleWeighting(task);
    }

    @Override
    protected void doStatistics(EntityPairExtend entityPair, List<MatchSentence> sentences){
        Integer D = sentences.size();
        for (PointWordExtend pointWordExtend : entityPair.getPointWordExtendList()){
            for (MatchSentence sentence : sentences){
                for (Lemma lemma : sentence.getLemmas()){
                    if (pointWordExtend.getPointWord().getLemma().equals(lemma.getLemma())){
                        pointWordExtend.setDw(pointWordExtend.getDw() + 1);
                        break;
                    }
                }
            }
        }

        //进行赋值计算
        for (PointWordExtend pointWordExtend : entityPair.getPointWordExtendList()){
            if (pointWordExtend.getDw() == 0){
                pointWordExtend.setInverseDocumentFrequency(0.0);
            }else {
                Double idf = (double)D / pointWordExtend.getDw();
                idf = Math.log(idf);
                pointWordExtend.setInverseDocumentFrequency(idf);
            }

            //纯idf, 直接把统计值设为idf
            pointWordExtend.setStatisticValue(pointWordExtend.getInverseDocumentFrequency());
        }
    }



}
