package DealOriginFile;

import java.util.List;

/**
 * Created by alan on 16/7/17.
 */


/**
 * wbcao:原始文档的JSON串格式:
 * {
 *     "identifi":  //标记的关系对编号
 *     "docs":      //共同出现的文档list
 *              [
 *                  {
 *                      "content":  //文档内容
 *                      "docNum":   //原始文档编号
 *                  }
 *                  {
 *                      "content":
 *                      "docNum":
 *                  }
 *                  ...
 *              ]
 * }
 */
public class OriginFile {
    private String identifi;
    private List<Doc> docs;

    public String getIdentifi() {
        return identifi;
    }

    public List<Doc> getDocs() {
        return docs;
    }

    public void setIdentifi(String identifi) {
        this.identifi = identifi;
    }

    public void setDocs(List<Doc> docs) {
        this.docs = docs;
    }

    //文件中单个文档的类
    public class Doc{
        private String docNum;
        private String content;

        public String getDocNum() {
            return docNum;
        }

        public String getContent() {
            return content;
        }

        public void setDocNum(String docNum) {
            this.docNum = docNum;
        }

        public void setContent(String content) {
            this.content = content;
        }
    }


}
