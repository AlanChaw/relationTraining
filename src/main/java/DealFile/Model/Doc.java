package DealFile.Model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by alan on 16/7/18.
 */


//文件中单个文档的类
public class Doc{
    private String docNum;
    private String content;
    private List<Lemma> lemmaList;

    public void buildLemmaList(){
        this.lemmaList = new ArrayList<Lemma>();

        String[] originLemmas = this.content.split("\t");
        for (String originLemma : originLemmas){
            Lemma lemma = new Lemma(originLemma);
            lemmaList.add(lemma);
        }

    }

    public String getDocNum() {
        return docNum;
    }

    public String getContent() {
        return content;
    }

    public List<Lemma> getLemmaList() {
        return lemmaList;
    }

    public void setDocNum(String docNum) {
        this.docNum = docNum;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setLemmaList(List<Lemma> lemmaList) {
        this.lemmaList = lemmaList;
    }

    @Override
    public String toString() {
        String docString = "docNum:" + this.docNum + "\n";
        for (Lemma lemma : this.lemmaList){
            docString += lemma.toString() + '\t';
        }
        return docString;
    }
}