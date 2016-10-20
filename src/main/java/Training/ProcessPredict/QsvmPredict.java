package Training.ProcessPredict;

import QuadraticSVM.QSVM;
import SLR.SimpleLogisticRegression;
import Training.Filters.PredictFilter;
import Training.Model.EntityPairExtend;
import Training.Model.PointWordExtend;
import Training.Model.PredictTask;
import com.mathworks.toolbox.javabuilder.MWClassID;
import com.mathworks.toolbox.javabuilder.MWNumericArray;

import java.util.List;

/**
 * Created by alan on 10/20/16.
 */
public class QsvmPredict implements PredictFilter {


    public int handlePredict(PredictTask task) {

        List<EntityPairExtend> entityPairsToPredict = task.getEntityPairsToPredict();
        double [][] X = new double[entityPairsToPredict.size()][entityPairsToPredict.get(0).getPointWordExtendList().size()];
        for (int i = 0; i < entityPairsToPredict.size(); i++){
            for (int j = 0; j < entityPairsToPredict.get(0).getPointWordExtendList().size(); j++){
                X[i][j] = entityPairsToPredict.get(i).getPointWordExtendList().get(j).getStatisticValue();
            }
        }

        MWNumericArray x = null;
        MWNumericArray output = null;
        Object[] result = null;

        try {
            QSVM qsvm = null;
            qsvm = (QSVM)task.getParameters().get("qsvm");
            result = qsvm.QuadraticSVM(2);
            Object predictFunc = result[0];
            x = new MWNumericArray(X, MWClassID.DOUBLE);
            result = qsvm.QSVMPredict(1, predictFunc, x);
            output = (MWNumericArray)result[0];
            int[] results = output.getIntData();
            for (int i = 0; i < entityPairsToPredict.size(); i++){
                entityPairsToPredict.get(i).setPredictValue(results[i]);
            }

        }catch (Exception e){
            e.printStackTrace();
        }



        return 0;
    }
}
