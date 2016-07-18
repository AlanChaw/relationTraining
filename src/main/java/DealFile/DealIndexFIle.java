package DealFile;

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

    private List<Entity> entityList;

    private DealIndexFile() throws java.io.IOException{
        this.entityList = new ArrayList<Entity>();


        String result;
        File file = new File("file/relationsWithIndex.txt");
        FileReader fileReader = new FileReader(file);
        BufferedReader br = new BufferedReader(fileReader);
        while ((result = br.readLine()) != null){
            Entity entity = new Entity(result);
            entityList.add(entity);
//            System.out.println(entity.toString());
        }

    }

    public static synchronized DealIndexFile getInstance() throws java.io.IOException{
        if (instance == null){
            instance = new DealIndexFile();
        }

        return instance;
    }

    public List<Entity> getEntityList() {
        return entityList;
    }
}
