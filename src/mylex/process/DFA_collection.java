package mylex.process;

import mylex.exception.NotREsException;
import mylex.exception.NotTokenException;
import mylex.utility.*;

import java.util.*;

/**
 * Created by 61990 on 2017/10/27.
 */
public class DFA_collection {
    //保存处理过的expression
    private List<Token_DFA> DFA_Expressions;


    private List<Token_NFA> NFA_Expressions;


    public DFA_collection(List<Token_NFA> NFA_Expressions) {
        this.NFA_Expressions = NFA_Expressions;
        DFA_Expressions = new ArrayList<>();
    }

    /**
     * 处理后缀表达式到NFA
     */
    public void handle_NFA_to_DFA() throws NotREsException {
        for (int i = 0; i < NFA_Expressions.size(); i++) {
            try {
                Token_NFA NFA = NFA_Expressions.get(i);
                DFA_Expressions.add(new Token_DFA(NFA, NFA_to_DFA(NFA.getNFA())));

            } catch (Exception e) {
                e.printStackTrace();
                throw new NotREsException(i);
            }
        }
    }

    List<DFA> needHandleStates = new ArrayList<>();
    List<DFA> hasHandle = new ArrayList<>();

    private List<DFA_Edge> NFA_to_DFA(NFA nfa) {
        //保存所有DFA边
        List<DFA_Edge> DFA_Edges = new ArrayList<>();
        //所有边的 char 用来遍历
        List<Character> allEdge = new ArrayList<>();
        List<NFA_Edge> edges = nfa.getNFA_Edges();
        for (NFA_Edge edge : edges) {
            if (!allEdge.contains(edge.getValue())) {
                if (edge.getValue() != 'ε')
                    allEdge.add(edge.getValue());
            }
        }
        List<Integer> closure0 = new ArrayList<>();
        closure0.add(0);
        List<Integer> list0 = getClosure(closure0, edges);
        needHandleStates.add(new DFA(list0, StaticVal.getDFA_ID()));
        while (needHandleStates.size() != 0) {
            DFA dfa1 = needHandleStates.get(0);
            for (char edge : allEdge) {
                List<Integer> list = getNext(dfa1, edges, edge);
                DFA dfa2 = null;
                if (list.size() == 0) {
                    continue;
                } else {
                    list = getClosure(list, edges);
                    for (DFA dfa : needHandleStates) {
                        if (dfa.getList() .equals(list)) {
                            dfa2 = dfa;
                            break;
                        }
                    }
                    for (DFA dfa : hasHandle) {
                        if (dfa.getList().equals(list)) {
                            dfa2 = dfa;
                            break;
                        }
                    }
                }
                if (dfa2 == null) {
                    dfa2 = new DFA(list, StaticVal.getDFA_ID());
                    needHandleStates.add(dfa2);
                }
                DFA_Edges.add(new DFA_Edge(dfa1, dfa2, edge));
            }
            hasHandle.add(needHandleStates.get(0));
            needHandleStates.remove(0);
        }
//        StaticVal.setDFA_id(0);
        return DFA_Edges;
    }

    private List<Integer> getNext(DFA dfa, List<NFA_Edge> edges, char c) {
        List<Integer> startNodes = dfa.getList();
        List<Integer> nextNode = new ArrayList<>();
        for (NFA_Edge edge : edges) {
            if (startNodes.contains(edge.getFirst_id()) && edge.getValue() == c) {
                addIntegerToClosure(nextNode, edge.getSecond_id());
            }
        }
        return nextNode;
    }


    private List<Integer> getClosure(List<Integer> needHandle, List<NFA_Edge> NFAs) {
        List<Integer> closure = new ArrayList<>();
        for (Integer num : needHandle) {
            closure.add(num);
        }
        List<Integer> hasHandle = new ArrayList<>();
        while (needHandle.size() != 0) {
            for (int j = 0; j < NFAs.size(); j++) {
                if (NFAs.get(j).getFirst_id() == needHandle.get(0) && NFAs.get(j).getValue() == 'ε') {
                    int num = NFAs.get(j).getSecond_id();
                    if (!hasHandle.contains(num)) {
                        if (!needHandle.contains(num)) {
                            needHandle.add(num);
                        }
                        if (!closure.contains(num)) {
                            addIntegerToClosure(closure, num);
                        }
                    }
                }
            }
            hasHandle.add(needHandle.get(0));
            needHandle.remove(0);
        }
        return closure;
    }

    // 把下一个按序插入列表，如果存在就不插入
    private void addIntegerToClosure(List<Integer> closure, int num) {
        if (closure.size() == 0) {
            closure.add(num);
        } else if (num > closure.get(closure.size() - 1)) {
            closure.add(num);
        } else {
            for (int i = 0; i < closure.size(); i++) {
                if (num == closure.get(i)) {
                    break;
                }
                if (num < closure.get(i)) {
                    closure.add(i, num);
                    break;
                }
            }
        }
    }

    /**
     * 将所有的DFA最小化
     */
    public void minDFA() {
        for (Token_DFA token_dfa : DFA_Expressions) {
            token_dfa.setDfa_edges(minimizeDFA(token_dfa.getDfa_edges()));
        }
    }

    //记录最小组

    //判断一次中有没有分组操作
    boolean has_split;

    private List<DFA_Edge> minimizeDFA(List<DFA_Edge> edges) {
        List<List<Integer>> lists;
        lists = new ArrayList<>();
        List<Integer> left_ID = new ArrayList<>();
        List<Integer> right_ID = new ArrayList<>();
        for (DFA_Edge edge : edges) {
            if (edge.getDFA_first().isTerminal() == false) {
                addIntegerToClosure(left_ID, edge.getFirstID());
            } else {
                addIntegerToClosure(right_ID, edge.getFirstID());
            }
            if (edge.getDFA_second().isTerminal() == false) {
                addIntegerToClosure(left_ID, edge.getSecondID());
            } else {
                addIntegerToClosure(right_ID, edge.getSecondID());
            }
        }
        lists.add(left_ID);
        lists.add(right_ID);
        //判断一次中有没有分组操作
        has_split = true;
        while (has_split == true) {
            has_split = false;
            List<Integer> newInteger = new ArrayList<>();
            for (List<Integer> list : lists) {
                if (list.size() == 1) {
                    continue;
                }
                for (int i = 0; i < list.size() - 1; i++) {
                    List<DFA_Edge> list1 = getStartWithInt(list.get(i), edges);
                    for (int j = i + 1; j < list.size(); j++) {
                        List<DFA_Edge> list2 = getStartWithInt(list.get(j), edges);
                        if (isSame(list1, list2, lists)) {

                        } else {
                            if (has_split == false) {
                                lists.add(newInteger);
                            }
                            newInteger.add(list.get(j));
                            list.remove(j);
                            j--;
                            has_split = true;
                        }
                    }
                    if (has_split == true) {
                        break;
                    }
                }
                if (has_split == true) {
                    break;
                }
            }
        }
        List<DFA_Edge> res_edges = new ArrayList<>();
//        Min_Edge  e1 = new Min_Edge(edges.get(0));
//        min_edges.add(e1);
//        Min_Edge e2 = new Min_Edge(edges.get(0));
//        System.out.println(e2.equals(e1));
//        System.out.println(min_edges.contains(e2));

        for (List<Integer> list : lists) {
            if (list.size() == 1) {
                continue;
            } else {
                int num = list.get(0);
                for (int q = 1; q < list.size(); q++) {
                    int temp = list.get(q);
                    for (DFA_Edge edge : edges) {
                        if (edge.getFirstID() == temp) {
                            edge.setFirstID(num);
                            edge.getDFA_first().setID(num);
                        }
                        if (edge.getSecondID() == temp) {
                            edge.setSecondID(num);
                            edge.getDFA_second().setID(num);
                        }
                    }
                }
            }
        }
        for (DFA_Edge edge : edges) {
            if (!res_edges.contains(edge)) {
                res_edges.add(edge);
            }
        }
        return res_edges;
    }

    private boolean isSame(List<DFA_Edge> list1, List<DFA_Edge> list2, List<List<Integer>> lists) {
        if (list1.size() != list2.size()) {
            return false;
        }
        for (DFA_Edge temp1 : list1) {
            boolean hasChar = false;
            for (DFA_Edge temp2 : list2) {
                if (temp1.getEdgeChar() == temp2.getEdgeChar()) {
                    hasChar = true;
                    if (isInSameGroup(temp1.getSecondID(), temp2.getSecondID(), lists)) {

                    } else {
                        return false;
                    }
                }
            }
            if (!hasChar) {
                return false;
            }
        }
        return true;
    }

    /**
     * 判断两个尾部节点是否在一组里面
     */
    private boolean isInSameGroup(int first, int second, List<List<Integer>> lists) {
        for (List<Integer> list : lists) {
            if (list.contains(first) && list.contains(second)) {
                return true;
            }
        }
        return false;
    }

    //获得所有以num 开头的边的list
    private List<DFA_Edge> getStartWithInt(int num, List<DFA_Edge> edges) {
        List<DFA_Edge> list = new ArrayList<>();
        for (DFA_Edge edge : edges) {
            if (edge.getFirstID() == num) {
                list.add(edge);
            }
        }
        return list;
    }

    /**
     * 返回输入字符串的 Token 序列
     */
    public List<Token> getTokenList(String line, int lineNum) throws NotTokenException {
        List<Token> tokens = new ArrayList<>();
        while (line.length() != 0) {
            String temp = line;
            Token token = null;
            for (Token_DFA DFA_Edges : DFA_Expressions) {
                DFA now = DFA_Edges.getDfa_edges().get(0).getDFA_first();
                DFA next = getNextDFA(temp.charAt(0), DFA_Edges, now);
                if (next == null) {
                    continue;
                } else {
                    if(line.length()==1){
                        if(next.isTerminal()==true){
                            token =freshToken(token, DFA_Edges, temp);
                        }else{
                            continue;
                        }
                    }
                    DFA lastTerminal =null;
                    int lastPos = 1;
                    for (int i = 1; i < temp.length(); i++) {
                        now = next;
                        if(now.isTerminal()== true){
                            lastTerminal=now;
                            lastPos=i;
                        }
                        next = getNextDFA(temp.charAt(i), DFA_Edges, now);
                        if (next == null) {
                            if (lastTerminal==null) {
                                break;
                            } else {
                                token = freshToken(token, DFA_Edges, temp.substring(0, lastPos));
                                break;
                            }
                        }
                        if(i==temp.length()-1){
                            if(next.isTerminal()==true) {
                                token = freshToken(token, DFA_Edges, temp);
                            }else{
                                token = freshToken(token, DFA_Edges, temp.substring(0, lastPos));
                            }
                        }
                    }
                }
            }
            if(token!=null){
                tokens.add(token);
                if(line .equals( token.getText())){
                    line = "";
                }else {
                    line = line.substring(token.getText().length());
                }
            }else{
                throw new NotTokenException(lineNum);
            }
        }
        if (tokens.size() == 0) {
            throw new NotTokenException(lineNum);
        }
        return tokens;
    }

    private Token freshToken(Token token, Token_DFA dfa, String str) {
        if (token == null) {
            return new Token(dfa, str);
        } else if (token.getText().length() < str.length()) {
            return new Token(dfa, str);
        }else{
            return token;
        }
    }


    private DFA getNextDFA(char c ,Token_DFA token_dfa,DFA dfa){
        for(DFA_Edge edge:token_dfa.getDfa_edges()){
            if(edge.getFirstID()==dfa.getID()&& edge.getEdgeChar()==c){
                return edge.getDFA_second();
            }
        }
        return null;
    }

}
