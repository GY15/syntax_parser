package myLL1.process;

import myLL1.utility.Derivation;
import myLL1.utility.Sign;
import myLL1.utility.StaticVal;
import mylex.exception.NotFoundREsException;
import mylex.utility.Token_RE;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by 61990 on 2017/11/7.
 * 此类处理translate.文件，并初始化好初始状态
 * 消除左递归，提取最大公共左因子
 */
public class Basic {
    List<Derivation> expressions;
    List<String> tokens;
    List<Sign> signList;
    public Basic( List<String> tokens){
        expressions = new ArrayList<>();
        this.tokens = tokens;
        signList = new ArrayList<>();
    }
    /**
     * 操作序列
     *
    * */
    public void create() throws IOException,NotFoundREsException {
        readFile();
        eliminateLeftRecursion();
    }



    /**
     *从文本中读取文件
     */
    public void readFile() throws IOException, NotFoundREsException {
        FileReader fr = null;
        File file = new File("files/translate.y");
        fr = new FileReader(file);

        @SuppressWarnings("resource")
        BufferedReader br = new BufferedReader(fr);
        String temp = null;
        Sign head = null;
        List<Sign> tail= new ArrayList<>();
        while ((temp = br.readLine()) != null) {
            String s[] = temp.trim().split(" ");
            if(s[0].equals(";")){
                head = null;
                tail = new ArrayList<>();
            }else{
                int begin;
                if(!s[0].equals("|")){
                    head = new Sign(StaticVal.getID(s[0]),s[0],tokens.contains(s[0]));
                    signList.add(head);
                    begin = 2;
                }else{
                    begin = 1;
                }
                for (int i =begin;i<s.length;i++){
                    tail.add(new Sign(StaticVal.getID(s[i]),s[i],tokens.contains(s[i])));
                }
                expressions.add(new Derivation(StaticVal.getExp_id_ID(),head,tail));
                tail = new ArrayList<>();
            }
        }
//        System.out.println(StaticVal.getTokensNeedVal().size());
    }
    /**
     * 消除左递归
     */
    private void eliminateLeftRecursion() {
        for (int i = 0; i<signList.size();i++){
            List<Derivation> list1 = moveOutBeginWith(signList.get(i));
            for (int j = 0;j<i-1; j++){
                List<Derivation> list2 = moveOutBeginWith(signList.get(j));
                for (int k = 0;k<list1.size();k++){
                    list1 = deriveExpression(list1,list2);
                }
                addDerivation(list2);
            }
            //消除list1的直接左递归
            list1 = directRecursion(list1);
            addDerivation(list1);

        }
    }

    private List<Derivation> deriveExpression(List<Derivation> list1, List<Derivation> list2) {
        List<Derivation> newList= new ArrayList<>();
        for (Derivation derivation : list1){
            for (int i = 0; i<list2.size();i++){
                if(derivation.getTail().get(0).equals(list2.get(i).getHead())) {
                    List<Sign> signs = combineSign(derivation.getTail(),list2.get(i).getTail());
                    newList.add(new Derivation(StaticVal.getExp_id_ID(),derivation.getHead(),signs));
                }
            }
        }
        return newList;
    }

    private List<Sign> combineSign(List<Sign> tail1, List<Sign> tail2) {
        List<Sign> signs = tail2;
        for(Sign sign :tail1){
            signs.add(sign);
        }
        return signs;
    }

    private void addDerivation(List<Derivation> list) {
        for (Derivation derivation : list){
            expressions.add(derivation);
        }
    }


    private List<Derivation>  moveOutBeginWith(Sign sign) {
        List<Derivation> list = new ArrayList<>();
        for (int i = 0; i < expressions.size();i++){
            if (expressions.get(i).getHead().equals(sign)){
                list.add(expressions.get(i));
                expressions.remove(i);
                i--;
            }
        }
        return list;
    }

    private List<Derivation> directRecursion(List<Derivation> derivations){
//        for (int i = 0;i < expressions.size() ; i++){
//            if (expressions.get(i).getHead().equals(sign) && expressions.get(i).getTail().get(0).equals(sign)){
//
//            }
//        }
        return null;
    }
}
