package Training.ProcessPredict;

import SLR.SimpleLogisticRegression;
import Training.Filters.PredictFilter;
import Training.Model.EntityPairExtend;
import Training.Model.PredictTask;
import com.mathworks.toolbox.javabuilder.MWClassID;
import com.mathworks.toolbox.javabuilder.MWNumericArray;

import java.util.List;

/**
 * Created by alan on 16/10/13.
 */
public class LogisticRegressionPredict implements PredictFilter{
    public int handlePredict(PredictTask task) {

        double[] theta = (double[])task.getParameters().get("theta");

        List<EntityPairExtend> entityPairsToPredict = task.getEntityPairsToPredict();
        double [][] X = new double[entityPairsToPredict.size()][entityPairsToPredict.get(0).getPointWordExtendList().size()];
        for (int i = 0; i < entityPairsToPredict.size(); i++){
            for (int j = 0; j < entityPairsToPredict.get(i).getPointWordExtendList().size(); j++){
                X[i][j] = entityPairsToPredict.get(i).getPointWordExtendList().get(j).getStatisticValue();
            }
        }
        MWNumericArray X_Matrix = null;
        MWNumericArray Theta = null;
        MWNumericArray output = null;
        Object[] result = null;
        double predictResults[] = new double[entityPairsToPredict.size()];
        try {
            SimpleLogisticRegression slr = null;
            slr = new SimpleLogisticRegression();
            X_Matrix = new MWNumericArray(X, MWClassID.DOUBLE);
            Theta = new MWNumericArray(theta, MWClassID.DOUBLE);
            result = slr.predict(1, X_Matrix, Theta);
            output = (MWNumericArray)result[0];
            predictResults = output.getDoubleData();
            for (int i = 0; i < predictResults.length; i++){
                if (predictResults[i] >= 0.5)
                    predictResults[i] = 1;
                else
                    predictResults[i] = 0;
            }

        }catch (Exception e){
            e.printStackTrace();
        }

        for (int i = 0; i < entityPairsToPredict.size(); i++){
            entityPairsToPredict.get(i).setPredictValue((int)predictResults[i]);
        }

//        for (EntityPairExtend entityPair : entityPairsToPredict){
//            double[] parameters = new double[entityPair.getPointWordExtendList().size()];
//            for (int i = 0; i < entityPair.getPointWordExtendList().size(); i++){
//                parameters[i] = entityPair.getPointWordExtendList().get(i).getStatisticValue();
//            }
//            MWNumericArray X = null;
//            MWNumericArray Theta = null;
//            MWNumericArray output = null;
//            Object[] result = null;
//            try {
//                SimpleLogisticRegression slr = null;
//                slr = new SimpleLogisticRegression();
//                X = new MWNumericArray(parameters, MWClassID.DOUBLE);
//                Theta = new MWNumericArray(theta, MWClassID.DOUBLE);
//                int[] p = X.getDimensions();
//                int[] q = Theta.getDimensions();
//                result = slr.predict(1, X, Theta);
//                output = (MWNumericArray)result[0];
//                double[] results = output.getDoubleData();
//                for (int i = 0; i < results.length; i++){
//                    if (results[i] >= 0.5)
//                        results[i] = 1;
//                    else
//                        results[i] = 0;
//                }
//                entityPair.setPredictValue(res);
//            }catch (Exception e){
//                e.printStackTrace();
//            }
//
//
//        }


        return 0;

    }
}
