package DealFile;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by alan on 16/7/17.
 */


public class DealOriginFile {
    private static DealOriginFile instance;

    //最后一个文档编号(为实际文档数量减一)
    private final static int DOCNUM = 10;
    private final static String FILEPATH = "/Users/alan/Documents/relation/relationFiles/OriginalDocWithIndex/";
    private final static String FILENAME = "OriginalDocWithIndex";

    private List<String> filePathList;
    private List<OriginFile> originFileList;

    private DealOriginFile() throws java.io.IOException{
        this.filePathList = new ArrayList<String>();
        for (int i = 0; i <= DOCNUM; i++){
            String fileName =FILEPATH + FILENAME + i;
            filePathList.add(fileName);
        }

        this.originFileList = new ArrayList<OriginFile>();

        generateOriginFiles();
    }

    public static synchronized DealOriginFile getInstance() throws java.io.IOException{
        if (instance == null){
            instance = new DealOriginFile();
        }
        return instance;
    }


    public void generateOriginFiles() throws java.io.IOException{

        for (String filePath : filePathList){
            String result;
            File file = new File(filePath);
            FileReader fileReader = new FileReader(file);
            BufferedReader br = new BufferedReader(fileReader);
            result = br.readLine();

            JSONObject object = new JSONObject(result);
            OriginFile originFileModel = fileJsonToModel(object);
            originFileList.add(originFileModel);
            String identifi = object.getString("identifi");
            System.out.println("处理文档" + identifi);
        }
    }

    public OriginFile fileJsonToModel(JSONObject object){
        OriginFile fileModel = new OriginFile();
        String identifi = object.getString("identifi");

        List<Doc> docs = new ArrayList<Doc>();
        JSONArray docsArray = object.getJSONArray("docs");
        for (int i = 0; i < docsArray.length(); i++){
            JSONObject docJson = docsArray.getJSONObject(i);
            Doc doc = new Doc();
            doc.setContent(docJson.getString("content"));
            doc.setDocNum(docJson.getString("docNum"));
            doc.buildLemmaList();

            docs.add(doc);
        }

        fileModel.setDocs(docs);
        fileModel.setIdentifi(identifi);

        return fileModel;
    }

    public List<OriginFile> getOriginFileList() {
        return originFileList;
    }

    public void printFileModel(int index){
        OriginFile file = this.originFileList.get(index);
        System.out.println(file.toString());

    }
}
