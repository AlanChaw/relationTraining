package Training.ProcessTraining;

import DealFile.Model.Lemma;
import Training.Model.MatchSentence;
import Training.Model.PointWordExtend;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by alan on 16/8/28.
 */
public class PureTF2 extends PureTF {


    @Override
    public void doTheTraining(List<MatchSentence> sentences, Integer relation) {
        List<PointWordExtend> pointWordList = new ArrayList<PointWordExtend>();
//        if (relation < 0){
//            pointWordList = competeExtendedPonintWords;
//        }
//        else {
//            pointWordList = cooperateExtendedPointWords;
//        }
        pointWordList = trainingTask.getPointWordExtendList();
        for (MatchSentence sentence : sentences){
            for (PointWordExtend wordExtend : pointWordList){
                for (Lemma lemma : sentence.getLemmas()){
                    if (wordExtend.getPointWord().getLemma().equals(lemma.getLemma())){
                        wordExtend.setAppearCount(wordExtend.getAppearCount() + 1);
                        break;
                    }
                }
            }


        }
    }

    @Override
    public void doStatistic(Integer sentencesNum) {
        System.out.println("两实体共同出现的句子: " + sentencesNum + "条");
        for (PointWordExtend pointWordExtend : trainingTask.getPointWordExtendList()) {
            if (pointWordExtend.getAppearCount() > 0) {
                System.out.println("词汇 " + pointWordExtend.getPointWord().getLemma() + " 出现: " + pointWordExtend.getAppearCount() + " 次");
            }
        }
        System.out.println("------------------");

        caculateValue(trainingTask.getPointWordExtendList(), sentencesNum);

    }
}
