package Training.ProcessWeighting.MLMethod;

import DealFile.Model.Doc;
import DealFile.Model.EntityPair;
import DealFile.Model.Lemma;
import DealFile.Model.OriginFile;
import PointerWord.PointWord;
import Training.Filters.WeightingFilterML;
import Training.Model.EntityPairExtend;
import Training.Model.MatchSentence;
import Training.Model.PointWordExtend;
import Training.Model.WeightingTask;
import Training.ProcessWeighting.HelpMethods;
import com.sun.org.apache.xerces.internal.impl.xpath.regex.Match;
import com.sun.tools.corba.se.idl.IncludeGen;
import com.sun.tools.jdi.IntegerTypeImpl;

import javax.swing.text.html.parser.Entity;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by alan on 16/10/13.
 */
public class MLPureTF implements WeightingFilterML{
    private List<PointWordExtend> pointWordExtends;
    protected WeightingTask weightingTask;

    public int handleWeighting(WeightingTask task) {
        this.pointWordExtends = new ArrayList<PointWordExtend>();
        this.weightingTask = task;

        for (EntityPair entityPair : weightingTask.getEntityPairSetCompete()){
            EntityPairExtend entityPairExtend = new EntityPairExtend();
            entityPairExtend.setEntityPair(entityPair);
            competeEntityPairsWithWeightings.add(entityPairExtend);
        }
        for (EntityPair entityPair : weightingTask.getEntityPairSetCooperate()){
            EntityPairExtend entityPairExtend = new EntityPairExtend();
            entityPairExtend.setEntityPair(entityPair);
            cooperateEntityPairsWithWeightings.add(entityPairExtend);
        }

        this.pointWordExtends.addAll(this.weightingTask.getPointWordExtendListCompete());
        this.pointWordExtends.addAll(this.weightingTask.getPointWordExtendListCooperate());

        doWeightings(competeEntityPairsWithWeightings);
        doWeightings(cooperateEntityPairsWithWeightings);

        return 1;
    }

    protected void doWeightings(List<EntityPairExtend> entityPairExtends){
        for (EntityPairExtend entityPairExtend : entityPairExtends){
            Integer identify = Integer.valueOf(entityPairExtend.getEntityPair().getIdentifi());
            OriginFile originFile = HelpMethods.fileJsonToModel(weightingTask.getOriginFileList().get(identify));
            List<MatchSentence> allSentences = new ArrayList<MatchSentence>();
            for (Doc doc : originFile.getDocs()){
                List<MatchSentence> sentences = HelpMethods.findSentencesInDoc(doc, entityPairExtend.getEntityPair());
                allSentences.addAll(sentences);
            }

            doStatistics(entityPairExtend, allSentences);
        }

    }

    protected void doStatistics(EntityPairExtend entityPairExtend, List<MatchSentence> allSentences){
        //这里必须用深复制
        List<PointWordExtend> pointwordInEntityPair = new ArrayList<PointWordExtend>();
        Iterator<PointWordExtend> iterator = this.pointWordExtends.iterator();
        while (iterator.hasNext()){
            pointwordInEntityPair.add(iterator.next().clone());
        }

        for (MatchSentence sentence : allSentences){
            for (Lemma lemma : sentence.getLemmas()){
                for (PointWordExtend word : pointwordInEntityPair){
                    if (word.getPointWord().getLemma().equals(lemma.getLemma())){
                        word.setAppearCount(word.getAppearCount() + 1);
                    }
                }
            }
        }

        int sum = 0;
        for (PointWordExtend pointWordExtend : pointwordInEntityPair){
            sum += pointWordExtend.getAppearCount();
        }
        for (PointWordExtend pointWordExtend : pointwordInEntityPair){
            if (sum == 0){
                pointWordExtend.setTermFrequency(0.0);
            }else {
                pointWordExtend.setTermFrequency((double) pointWordExtend.getAppearCount() / sum);
            }
            //纯tf方法,将统计值直接设为tf值
            pointWordExtend.setStatisticValue(pointWordExtend.getTermFrequency());
        }

        entityPairExtend.setPointWordExtendList(pointwordInEntityPair);
    }

}
