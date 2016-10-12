package Training.ProcessWeighting;

import DealFile.Model.Doc;
import DealFile.Model.EntityPair;
import DealFile.Model.Lemma;
import DealFile.Model.OriginFile;
import Training.Model.MatchSentence;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static Training.ProcessWeighting.PureTF.WINDOWLENGTH;

/**
 * Created by alan on 16/9/10.
 */
public class HelpMethods {

    /**
     * 把原始文档的json串封装成模型
     * @param object
     * @return
     */
    public static OriginFile fileJsonToModel(JSONObject object){
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

        System.out.println("转换文档 " + identifi);

        return fileModel;

    }

    /**
     * 在某一关系对所出现的某一个文档中,找出在给定窗口长度下关系对中两个实体共同出现的句子
     * @param doc   给定的文档
     * @param entityPair    某一特定的实体对
     * @return 在该文档中所有匹配的句子
     */
    public static List<MatchSentence> findSentencesInDoc(Doc doc, EntityPair entityPair){
        List<MatchSentence> sentences = new ArrayList<MatchSentence>();

        //遍历整个文档,直接检索,找到实体词出现的位置
        for (int i = 0; i < doc.getLemmaList().size(); i++){
            Lemma lemma = doc.getLemmaList().get(i);
            if (lemma.getLemma().equals(entityPair.getEntityName_1())){
                //该词窗口的下界
                Integer lowerBound = (i >= WINDOWLENGTH) ? (i - WINDOWLENGTH) : 0;
                //该词窗口的上界(实际是下标,便于操作)
                Integer upperBound = ((i + WINDOWLENGTH + 1) <= doc.getLemmaList().size()) ? (i + WINDOWLENGTH) : (doc.getLemmaList().size() - 1);

                //设置句子
                MatchSentence sentence = new MatchSentence();
                sentence.setRelation(entityPair.getRelation());
                for (int j = lowerBound; j <= upperBound; j++){
                    sentence.addLemma(doc.getLemmaList().get(j));
                }

                //检索句子中是否有另一个实体词,如果有则保留
                for (int j = 0; j < sentence.getLemmas().size(); j++){
                    if (sentence.getLemmas().get(j).getLemma().equals(entityPair.getEntityName_2()))
                        sentences.add(sentence);
                }
            }

        }
        return sentences;
    }




}
