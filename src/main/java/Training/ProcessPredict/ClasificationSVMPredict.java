package Training.ProcessPredict;

import CSVM.SVM;
import QuadraticSVM.QSVM;
import Training.Filters.PredictFilter;
import Training.Model.EntityPairExtend;
import Training.Model.PredictTask;
import com.mathworks.toolbox.javabuilder.MWClassID;
import com.mathworks.toolbox.javabuilder.MWNumericArray;

import java.util.List;

/**
 * Created by alan on 10/24/16.
 */
public class ClasificationSVMPredict implements PredictFilter {
    public static double classificationThreshold;

    public int handlePredict(PredictTask task) {

        List<EntityPairExtend> entityPairsToPredict = task.getEntityPairsToPredict();
        double [][] X = new double[entityPairsToPredict.size()][entityPairsToPredict.get(0).getPointWordExtendList().size()];
        for (int i = 0; i < entityPairsToPredict.size(); i++){
            for (int j = 0; j < entityPairsToPredict.get(0).getPointWordExtendList().size(); j++){
                X[i][j] = entityPairsToPredict.get(i).getPointWordExtendList().get(j).getStatisticValue();
            }
        }

        MWNumericArray x = null;
        MWNumericArray labels = null;
        MWNumericArray scores = null;
        Object[] result = null;

        try {
            SVM csvm = null;
            csvm = (SVM)task.getParameters().get("csvm");
            result = csvm.CSVM(1);
            Object Mdl = result[0];
            x = new MWNumericArray(X, MWClassID.DOUBLE);
            result = csvm.CSVMPredict(2, Mdl, x);
            labels = (MWNumericArray)result[0];
            scores = (MWNumericArray)result[1];
            int[] labelsArray = labels.getIntData();
            double[] scoresArray = scores.getDoubleData();

            for (int i = 0; i < entityPairsToPredict.size(); i++){
                if (scoresArray[i] < classificationThreshold)
                    entityPairsToPredict.get(i).setPredictValue(0);
                else
                    entityPairsToPredict.get(i).setPredictValue(1);
            }


        }catch (Exception e){
            e.printStackTrace();
        }

        return 0;
    }
}