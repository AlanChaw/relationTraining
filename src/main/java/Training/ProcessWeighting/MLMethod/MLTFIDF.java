package Training.ProcessWeighting.MLMethod;

import DealFile.Model.Doc;
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
public class MLTFIDF extends MLWeighting implements WeightingFilterML {
    public int handleWeighting(WeightingTask task) {
        return super.handleWeighting(task);
    }


    @Override
    protected void doStatistics(EntityPairExtend entityPairExtend, List<MatchSentence> allSentences){
        MLPureTF mlPureTF = new MLPureTF();
        MLPureIDF mlPureIDF = new MLPureIDF();
        mlPureTF.doStatistics(entityPairExtend, allSentences);
        mlPureIDF.doStatistics(entityPairExtend, allSentences);

        for (PointWordExtend pointWordExtend : entityPairExtend.getPointWordExtendList()){
            Double TF_IDF = pointWordExtend.getTermFrequency() * pointWordExtend.getInverseDocumentFrequency();
            pointWordExtend.setStatisticValue(TF_IDF);
        }

    }



}
