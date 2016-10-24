package Training.ProcessPredict;

import CSVM.SVM;
import DecisionTree.DT;
import Training.Filters.PredictFilter;
import Training.Model.EntityPairExtend;
import Training.Model.PredictTask;
import com.mathworks.toolbox.javabuilder.MWClassID;
import com.mathworks.toolbox.javabuilder.MWNumericArray;

import java.util.List;

/**
 * Created by alan on 10/24/16.
 */
public class ClassificationTreePredict implements PredictFilter {


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
            DT decisionTree = null;
            decisionTree = (DT)task.getParameters().get("dt");
            result = decisionTree.DecisionTree(1);
            Object Mdl = result[0];
            x = new MWNumericArray(X, MWClassID.DOUBLE);
            result = decisionTree.DecisionTreePredict(1, Mdl, x);
            output = (MWNumericArray)result[0];
            int[] results = output.getIntData();
            for (int i = 0; i < entityPairsToPredict.size(); i++){
                entityPairsToPredict.get(i).setPredictValue(results[i]);
            }

            decisionTree.dispose();
            output.dispose();
        }catch (Exception e){
            e.printStackTrace();
        }

        return 0;
    }
}