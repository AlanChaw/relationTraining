package DealFile;

import DealFile.Model.EntityPair;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by alan on 16/7/18.
 */
public class DealIndexFile {

    public static DealIndexFile instance;

    private List<EntityPair> entityPairList;

    private DealIndexFile() throws java.io.IOException{
        this.entityPairList = new ArrayList<EntityPair>();

        String result;
        File file = new File("/Users/alan/Documents/relationTraining/file/relationsWithIndex.txt");
        FileReader fileReader = new FileReader(file);
        BufferedReader br = new BufferedReader(fileReader);
        while ((result = br.readLine()) != null){
            EntityPair entityPair = new EntityPair(result);

            if (Integer.valueOf(entityPair.getIdentifi()) >= DealOriginFile.DOCNUM)
                break;
            entityPairList.add(entityPair);
//            System.out.println(entityPair.toString());
        }

    }

    public static synchronized DealIndexFile getInstance() throws java.io.IOException{
        if (instance == null){
            instance = new DealIndexFile();
        }

        return instance;
    }

    public List<EntityPair> getEntityPairList() {
        return entityPairList;
    }
}
