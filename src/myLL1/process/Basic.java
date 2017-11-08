package myLL1.process;

import myLL1.exception.LeftRecursionException;
import myLL1.utility.Derivation;
import myLL1.utility.Sign;
import myLL1.utility.StaticVal;
import mylex.exception.NotFoundREsException;

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

    public Basic(List<String> tokens) {
        expressions = new ArrayList<>();
        this.tokens = tokens;
        signList = new ArrayList<>();
        StaticVal.init();
    }

    /**
     * 操作序列
     */
    public void create() throws IOException, NotFoundREsException, LeftRecursionException {
        readFile();
        eliminateLeftRecursion();
        pickMaxLeft();
    }


    /**
     * 从文本中读取文件
     */
    public void readFile() throws IOException, NotFoundREsException {
        FileReader fr = null;
        File file = new File("files/translate.y");
        fr = new FileReader(file);

        @SuppressWarnings("resource")
        BufferedReader br = new BufferedReader(fr);
        String temp = null;
        Sign head = null;
        List<Sign> tail = new ArrayList<>();
        while ((temp = br.readLine()) != null) {
            String s[] = temp.trim().split(" ");
            if (s[0].equals(";")) {
                head = null;
                tail = new ArrayList<>();
            } else {
                int begin;
                if (!s[0].equals("|")) {
                    head = new Sign(StaticVal.getID(s[0]), s[0], tokens.contains(s[0]));
                    signList.add(head);
                    begin = 2;
                } else {
                    begin = 1;
                }
                for (int i = begin; i < s.length; i++) {
                    tail.add(new Sign(StaticVal.getID(s[i]), s[i], tokens.contains(s[i])));
                }
                expressions.add(new Derivation(StaticVal.getExp_id_ID(), head, tail));
                tail = new ArrayList<>();
            }
        }
        StaticVal.firstSign = expressions.get(0).getHead();
//        System.out.println(StaticVal.getTokensNeedVal().size());
    }

    /**
     * 消除左递归
     */
    private void eliminateLeftRecursion() throws LeftRecursionException {
        for (int i = 0; i < signList.size(); i++) {
            List<Derivation> list1 = moveOutBeginWith(signList.get(i));
            for (int j = 0; j < i - 1; j++) {
                List<Derivation> list2 = moveOutBeginWith(signList.get(j));
                for (int k = 0; k < list1.size(); k++) {
                    list1 = deriveExpression(list1, list2);
                }
                addDerivation(list2);
            }
            //消除list1的直接左递归
            list1 = directRecursion(list1);
            addDerivation(list1);
        }
    }

    private List<Derivation> deriveExpression(List<Derivation> list1, List<Derivation> list2) {
        List<Derivation> newList = new ArrayList<>();
        for (Derivation derivation : list1) {
            boolean hasHandle = false;
            for (int i = 0; i < list2.size(); i++) {
                if (derivation.getTail().get(0).equals(list2.get(i).getHead())) {
                    hasHandle = true;
                    List<Sign> signs = combineSign(derivation.getTail(), list2.get(i).getTail());
                    newList.add(new Derivation(StaticVal.getExp_id_ID(), derivation.getHead(), signs));
                }
            }
            if (!hasHandle) {
                newList.add(derivation);
            }
        }
        return newList;
    }

    private List<Sign> combineSign(List<Sign> tail1, List<Sign> tail2) {
        List<Sign> signs = tail2;
        for (Sign sign : tail1) {
            signs.add(sign);
        }
        return signs;
    }

    private void addDerivation(List<Derivation> list) {
        for (Derivation derivation : list) {
            expressions.add(derivation);
        }
    }


    private List<Derivation> moveOutBeginWith(Sign sign) {
        List<Derivation> list = new ArrayList<>();
        for (int i = 0; i < expressions.size(); i++) {
            if (expressions.get(i).getHead().equals(sign)) {
                list.add(expressions.get(i));
                expressions.remove(i);
                i--;
            }
        }
        return list;
    }

    private List<Derivation> directRecursion(List<Derivation> derivations) throws LeftRecursionException {
        List<Derivation> recursions = new ArrayList<>();
        List<Derivation> common = new ArrayList<>();
        for (int i = 0; i < derivations.size(); i++) {
            if (derivations.get(i).getHead().equals(derivations.get(i).getTail().get(0))) {
                recursions.add(derivations.get(i));
            } else {
                common.add(derivations.get(i));
            }
        }
        if (recursions.size() == 0) {
            return common;
        } else if (common.size() == 0) {
            throw new LeftRecursionException();
        } else {
            List<Derivation> result = new ArrayList<>();
            String newString = recursions.get(0).getHead().getVal() + ".";
            Sign sign = new Sign(StaticVal.getID(newString), newString, false);
            for (Derivation derivation : common) {
                List<Sign> l = derivation.getTail();
                l.add(sign);
                derivation.setTail(l);
                result.add(derivation);
            }
            for (Derivation derivation : recursions) {
                List<Sign> l = derivation.getTail();
                l.remove(0);
                if (l.size() == 0) {
                    throw new LeftRecursionException();
                }
                l.add(sign);
                result.add(new Derivation(StaticVal.getExp_id_ID(), sign, l));
            }
            List<Sign> n = new ArrayList<>();
            n.add(StaticVal.getNullSign());
            result.add(new Derivation(StaticVal.getExp_id_ID(), sign, n));
            return result;
        }
    }

    /**
     * 提取最大公共左因子
     */
    private void pickMaxLeft() {
        for (Derivation derivation : expressions) {
            if (!signList.contains(derivation.getHead())) {
                signList.add(derivation.getHead());
            }
        }
        for (Sign sign : signList) {
            List<Derivation> derivations = moveOutBeginWith(sign);
            if (derivations.size() != 1) {
                derivations = findMaxMatch(derivations);
            }
            addDerivation(derivations);
        }
    }

    private List<Derivation> findMaxMatch(List<Derivation> derivations) {
        List<Derivation> list = derivations;
        List<Sign> max = new ArrayList<>();
        while (true) {
            for (int j = 0; j < list.size() - 1; j++) {
                for (int i = j + 1; i < list.size(); i++) {
                    List<Sign> match = matchMax(list.get(j).getTail(), list.get(i).getTail());
                    if (max.size() < match.size()) {
                        max = match;
                    }
                }
            }
            if (max.size() == 0) {
                return list;
            } else {
                list = pickStartWith(list, max);
            }
        }
    }

    private List<Derivation> pickStartWith(List<Derivation> derivations, List<Sign> maxSigns) {
        List<Derivation> list = new ArrayList<>();
        Sign newSign = new Sign(StaticVal.getID(derivations.get(0).getHead().getVal()), derivations.get(0).getHead().getVal(), false);
//        signList.add(newSign);
        for (int i = 0; i < derivations.size(); i++) {
            if (isBeginWith(derivations.get(i), maxSigns)) {
                List<Sign> signs = new ArrayList<>();
                List<Sign> tail = derivations.get(i).getTail();
                if (maxSigns.size() == tail.size()) {
                    signs.add(StaticVal.getNullSign());
                } else {
                    for (int j = maxSigns.size(); j < tail.size(); j++) {
                        signs.add(tail.get(j));
                    }
                }
                expressions.add(new Derivation(StaticVal.getExp_id_ID(), newSign, signs));
            } else {
                list.add(derivations.get(i));
            }
        }
        maxSigns.add(newSign);
        list.add(new Derivation(StaticVal.getExp_id_ID(), derivations.get(0).getHead(), maxSigns));
        return list;
    }

    //是否以对应的最大项匹配
    private boolean isBeginWith(Derivation derivation, List<Sign> maxSigns) {
        if (matchMax(derivation.getTail(), maxSigns).size() == maxSigns.size()) {
            return true;
        }
        return false;
    }

    /**
     * 找出最大匹配项
     */
    private List<Sign> matchMax(List<Sign> firstSign, List<Sign> tail) {
        List<Sign> signs = new ArrayList<>();
        for (int i = 0; i < firstSign.size() && i < tail.size(); i++) {
            if (firstSign.get(i).equals(tail.get(i))) {
                signs.add(firstSign.get(i));
            } else {
                break;
            }
        }
        return signs;
    }

    public List<Derivation> getExpressions() {
        return expressions;
    }
}
