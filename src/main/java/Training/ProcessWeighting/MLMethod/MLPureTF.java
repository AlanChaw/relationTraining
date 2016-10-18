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
public class MLPureTF extends MLWeighting implements WeightingFilterML {
//    protected List<PointWordExtend> pointWordExtends;
//    protected WeightingTask weightingTask;

    public int handleWeighting(WeightingTask task) {
        return super.handleWeighting(task);

    }

    @Override
    protected void doStatistics(EntityPairExtend entityPairExtend, List<MatchSentence> allSentences){
        for (MatchSentence sentence : allSentences){
            for (Lemma lemma : sentence.getLemmas()){
                for (PointWordExtend word : entityPairExtend.getPointWordExtendList()){
                    if (word.getPointWord().getLemma().equals(lemma.getLemma())){
                        word.setAppearCount(word.getAppearCount() + 1);
                    }
                }
            }
        }

        int sum = 0;
        for (MatchSentence sentence : allSentences){
            sum += sentence.getLemmas().size();
        }

        //进行赋值
        for (PointWordExtend pointWordExtend : entityPairExtend.getPointWordExtendList()){
            if (pointWordExtend.getAppearCount() == 0){
                pointWordExtend.setTermFrequency(0.0);
            }else {
                pointWordExtend.setTermFrequency((double) pointWordExtend.getAppearCount() / sum);
            }
            //纯tf方法,将统计值直接设为tf值
            pointWordExtend.setStatisticValue(pointWordExtend.getTermFrequency());
        }

    }

}
