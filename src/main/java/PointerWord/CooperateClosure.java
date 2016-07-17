package PointerWord;

/**
 * Created by alan on 16/7/16.
 */

import net.sf.extjwnl.JWNLException;
import net.sf.extjwnl.data.IndexWord;
import net.sf.extjwnl.data.POS;
import net.sf.extjwnl.data.PointerUtils;
import net.sf.extjwnl.data.Word;
import net.sf.extjwnl.data.list.PointerTargetTree;
import net.sf.extjwnl.data.list.PointerTargetTreeNode;
import net.sf.extjwnl.dictionary.Dictionary;

import java.util.ArrayList;
import java.util.List;

/**
 * wbcao:合作指示词闭包
 */
public class CooperateClosure{
    //查找子树的深度
    private static final int DEPTH = 3;

    private final Dictionary dictionary;
    private IndexWord COOPERATE;
    private IndexWord ALLY;
    private IndexWord COLLABORATE;
    private IndexWord JOINT;
    private IndexWord OWN;
    private IndexWord PARTNER;
    private IndexWord COORDINATE;
    private IndexWord ENGAGE;
    private IndexWord PARTNERSHIP;
    private IndexWord AGREE;

    private List<IndexWord> indexWordList;
    private List<PointWord> pointWordList;
    private List<PointerTargetTree> pointerTargetTreeList;


    public CooperateClosure() throws JWNLException{
        this.dictionary = Dictionary.getDefaultResourceInstance();

        COOPERATE = dictionary.lookupIndexWord(POS.VERB, "cooperate");
        ALLY = dictionary.lookupIndexWord(POS.VERB, "ally");
        COLLABORATE = dictionary.lookupIndexWord(POS.VERB, "collaborate");
        JOINT = dictionary.lookupIndexWord(POS.ADJECTIVE, "joint");
        OWN = dictionary.lookupIndexWord(POS.VERB, "own");
        PARTNER = dictionary.lookupIndexWord(POS.NOUN, "partner");
        COORDINATE = dictionary.lookupIndexWord(POS.VERB, "coordinate");
        ENGAGE = dictionary.lookupIndexWord(POS.VERB, "engage");
        PARTNERSHIP = dictionary.lookupIndexWord(POS.NOUN, "partnership");
        AGREE = dictionary.lookupIndexWord(POS.VERB, "agree");

        this.indexWordList = new ArrayList<IndexWord>();
        this.indexWordList.add(COOPERATE);
        this.indexWordList.add(ALLY);
        this.indexWordList.add(COLLABORATE);
        this.indexWordList.add(JOINT);
        this.indexWordList.add(OWN);
        this.indexWordList.add(PARTNER);
        this.indexWordList.add(COORDINATE);
        this.indexWordList.add(ENGAGE);
        this.indexWordList.add(PARTNERSHIP);
        this.indexWordList.add(AGREE);

        build();

    }

    private void build() throws JWNLException{
        this.pointWordList = new ArrayList<PointWord>();

        this.pointerTargetTreeList = new ArrayList<PointerTargetTree>();
        for (IndexWord indexWord : indexWordList){
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

//    public void buildClosure(){
//        for (PointerTargetTree pointerTargetTree : pointerTargetTreeList){
////            PointerTargetNode rootNode = pointerTargetTree.getRootNode();
////            System.out.println(rootNode.getSynset());
//            PointerTargetTreeNode rootNode = pointerTargetTree.getRootNode();
//
//            Integer depth = Integer.valueOf(DEPTH);
//            generateTree(rootNode, depth);
//        }
//
//        for (PointWord pointWord : pointWordList){
//            pointWord.print();
//        }
//    }

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




}
