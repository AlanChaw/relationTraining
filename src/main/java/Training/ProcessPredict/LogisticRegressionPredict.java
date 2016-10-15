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

//        System.out.println(theta.toString());
//        for (double t : theta){
//            System.out.println(t);
//        }
        List<EntityPairExtend> entityPairsToPredict = task.getEntityPairsToPredict();
        for (EntityPairExtend entityPair : entityPairsToPredict){
            double[] parameters = new double[entityPair.getPointWordExtendList().size()];
            for (int i = 0; i < entityPair.getPointWordExtendList().size(); i++){
                parameters[i] = entityPair.getPointWordExtendList().get(i).getStatisticValue();
            }
            MWNumericArray X = null;
            MWNumericArray Theta = null;
            MWNumericArray output = null;
            Object[] result = null;
            try {
                SimpleLogisticRegression slr = null;
                slr = new SimpleLogisticRegression();
                X = new MWNumericArray(parameters, MWClassID.DOUBLE);
                Theta = new MWNumericArray(theta, MWClassID.DOUBLE);
                int[] p = X.getDimensions();
                int[] q = Theta.getDimensions();
                result = slr.predict(1, X, Theta);
                output = (MWNumericArray)result[0];
                int res = output.getInt();
                entityPair.setPredictValue(res);
            }catch (Exception e){
                e.printStackTrace();
            }


        }


        return 0;

    }
}
