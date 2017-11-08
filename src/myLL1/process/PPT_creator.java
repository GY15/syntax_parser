package myLL1.process;

import myLL1.exception.ConflictHappen;
import myLL1.utility.Derivation;
import myLL1.utility.PPT_item;
import myLL1.utility.Sign;
import myLL1.utility.StaticVal;

import java.beans.Expression;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by 61990 on 2017/11/7.
 */
public class PPT_creator {

    List<Derivation> expressions;
    List<PPT_item> ppt_items;

    public PPT_creator(List<Derivation> expressions) {
        this.expressions = expressions;
        ppt_items = new ArrayList<>();
    }

    /**
     * 初始化PPT
     */
    public void initPPT() {
        for (Derivation derivation : expressions) {
            List<Derivation> temp = new ArrayList<>();
            temp.add(derivation);
            List<Sign> signs = FIRST(temp);
            if (signs.contains(StaticVal.getNullSign())) {
                List<Sign> signs1 = FOLLOW(derivation.getHead());
                for (Sign sign : signs1) {
                    if (!sign.equals(StaticVal.getNullSign())) {
                        ppt_items.add(new PPT_item(derivation.getHead(), sign, derivation));
                    }
                }
            }
            for (Sign sign : signs) {
                if (!sign.equals(StaticVal.getNullSign())) {
                    ppt_items.add(new PPT_item(derivation.getHead(), sign, derivation));
                }
            }

        }
    }

    private List<Sign> FOLLOW(Sign head) {
        List<Sign> signs = getFollowSign(head);
        List<Sign> notTerminal;
        boolean hasNotTerminal = true;
        while (hasNotTerminal) {
            notTerminal = new ArrayList<>();
            for (int i = 0; i < signs.size(); i++) {
                if (!signs.get(i).isTerminal()) {
                    notTerminal.add(signs.get(i));
                    signs.remove(i);
                    i--;
                }
            }
            for (int j = 0; j < notTerminal.size(); j++) {

                signs = appendList(signs, getFollowSign(notTerminal.get(j)));
//                signs.remove(notTerminal.get(j));
//                j--;
            }
            hasNotTerminal = false;
            for (Sign sign : signs) {
                if (!sign.isTerminal()) {
                    hasNotTerminal = true;
                    break;
                }
            }
        }
        return signs;
    }

    private List<Sign> getFollowSign(Sign sign) {
        List<Sign> res = new ArrayList<>();
        if (sign.equals(StaticVal.firstSign)) {
            res.add(StaticVal.endOfSentence);
        }
        for (Derivation derivation : expressions) {
            List<Sign> signs = derivation.getTail();
            for (int i = 0; i < signs.size(); i++) {
                if (signs.get(i).equals(sign)) {
                    if (i == signs.size() - 1) {
                        if (derivation.getHead().equals(sign)) {
                        } else {
                            res.add(derivation.getHead());
                        }
                    } else {
                        List<Sign> temp = new ArrayList<>();
                        for (int j = i + 1; j < signs.size(); j++) {
                            temp.add(signs.get(j));
                        }
                        temp = FIRST2(temp);
                        if (temp.contains(StaticVal.getNullSign())) {
                            temp.remove(StaticVal.getNullSign());
                            temp.add(derivation.getHead());
                        }

                        res = appendList(res, temp);

                    }
                }
            }
        }
        return res;
    }

    private List<Sign> FIRST2(List<Sign> signs) {
        List<Sign> list = new ArrayList<>();
        List<Sign> tail = signs;
        for (int j = 0; j < tail.size(); j++) {
            if (tail.get(j).isTerminal()) {
                if (tail.get(j).getID() == 'ε') {
                    if (j != tail.size() - 1) {
                    } else {
                        list.add(tail.get(j));
                    }
                    continue;
                } else {
                    list.add(tail.get(j));
                    int rem = 1;
                    break;

                }
            } else {
                List<Derivation> d = getStartWith(tail.get(j));
                list = appendList(list, FIRST(d));
            }
            if (j != tail.size() - 1) {
                if (list.contains(StaticVal.getNullSign())) {
                    list.remove(StaticVal.getNullSign());
                } else {
                    break;
                }
            }
        }
        return list;
    }

    private List<Sign> FIRST(List<Derivation> derivations) {
        List<Sign> list = new ArrayList<>();
        for (int i = 0; i < derivations.size(); i++) {
            List<Sign> tail = derivations.get(i).getTail();
            for (int j = 0; j < tail.size(); j++) {
                if (tail.get(j).isTerminal()) {
                    if (tail.get(j).getID() == 'ε') {
                        if (j != tail.size() - 1) {
                        } else {
                            list.add(tail.get(j));
                        }
                        continue;
                    } else {
                        list.add(tail.get(j));
                        break;
                    }
                } else {
                    List<Derivation> d = getStartWith(tail.get(j));
                    list = appendList(list, FIRST(d));
                }
                if (j != tail.size() - 1) {
                    if (list.contains(StaticVal.getNullSign())) {
                        list.remove(StaticVal.getNullSign());
                    } else {
                        break;
                    }
                }
            }
        }

        return list;
    }

    private List<Derivation> getStartWith(Sign sign) {
        List<Derivation> derivations = new ArrayList<>();
        for (int i = 0; i < expressions.size(); i++) {
            if (expressions.get(i).getHead().equals(sign)) {
                derivations.add(expressions.get(i));
            }
        }
        return derivations;
    }

    private List<Sign> appendList(List<Sign> list, List<Sign> extra) {
        for (Sign sign : extra) {
            if (!list.contains(sign)) {
                list.add(sign);
            }
        }
        return list;
    }

    public List<PPT_item> getPpt_items() {
        return ppt_items;
    }

    public void check() throws ConflictHappen {
        for (int i = 0; i < ppt_items.size() - 1; i++) {
            for (int j = i+1; j < ppt_items.size(); j++) {
                PPT_item item1 = ppt_items.get(i);
                PPT_item item2 = ppt_items.get(j);
                if (item1.getStart().equals(item2.getStart()) && item1.getEnd().equals(item2.getEnd())) {
                    throw new ConflictHappen();
                }
            }
        }
    }
}
