package PointerWord;

import net.sf.extjwnl.JWNLException;
import net.sf.extjwnl.data.IndexWord;
import net.sf.extjwnl.data.PointerUtils;
import net.sf.extjwnl.data.Word;
import net.sf.extjwnl.data.list.PointerTargetTree;
import net.sf.extjwnl.data.list.PointerTargetTreeNode;
import net.sf.extjwnl.dictionary.Dictionary;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by alan on 16/7/17.
 */

public class PointWordClosure {

    protected final Dictionary dictionary;

    //查找子树的深度
    protected static final int DEPTH = 1;

    protected List<IndexWord> indexWordList;
    protected List<PointWord> pointWordList;
    protected List<PointerTargetTree> pointerTargetTreeList;


    public PointWordClosure() throws JWNLException{
        this.dictionary = Dictionary.getDefaultResourceInstance();
    }

    protected void build() throws JWNLException {
        this.pointWordList = new ArrayList<PointWord>();

        this.pointerTargetTreeList = new ArrayList<PointerTargetTree>();
        for (IndexWord indexWord : indexWordList){
            if (indexWord == null)
                continue;

            PointWord pointWord = new PointWord(indexWord.getLemma(), 0);
            pointWordList.add(pointWord);

            PointerTargetTree tree = PointerUtils.getHyponymTree(indexWord.getSenses().get(0));
            PointerTargetTreeNode rootNode = tree.getRootNode();
            Integer depth = Integer.valueOf(DEPTH);
            generateTree(rootNode, depth);

            pointerTargetTreeList.add(tree);
        }

//        for (IndexWord indexWord : indexWordList){
//            PointWord pointWord = new PointWord(indexWord.getLemma(), 0);
//            pointWordList.add(pointWord);
//        }
    }

    public void printAllTrees() throws JWNLException{
        for (PointerTargetTree pointerTargetTree : pointerTargetTreeList){
            System.out.println("------------------------");
            pointerTargetTree.print();
        }


    }

    public void printWordList(){

        for (PointWord pointWord : pointWordList){
            pointWord.print();
        }
    }


    private void generateTree(PointerTargetTreeNode rootNode, Integer depth){
        if (depth.equals(0))
            return;

        for (Word word : rootNode.getSynset().getWords()){
            PointWord pointWord = new PointWord(word.getLemma(), DEPTH - depth + 1);
            if (alreadyExist(pointWord))
                continue;
            else
                pointWordList.add(pointWord);
        }

        List<PointerTargetTreeNode> childNodeList = rootNode.getChildTreeList();
        for (PointerTargetTreeNode childNode : childNodeList){
            generateTree(childNode, depth - 1);
        }


    }

    /**
     * wbcao:检测要添加的指示词是否已存在于集合中
     * @param pointWord
     * @return
     */
    private boolean alreadyExist(PointWord pointWord){
        for (PointWord pointWordInList : pointWordList){
            if (pointWord.getLemma().equals(pointWordInList.getLemma()))
                return true;
        }
        return false;

    }

    public List<PointWord> getPointWordList() {
        return pointWordList;
    }
}
