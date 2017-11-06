package mylex.utility;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 61990 on 2017/10/27.
 */
public class NFA {
    private List<NFA_Edge> NFA_Edges = new ArrayList<>();


    /**
     * 遇到单目运算符号 * ？ +  时候
     *
     *
     * */
    public NFA(NFA nfa, char c) {
        int start = StaticVal.getNFA_ID();
        int end = StaticVal.getNFA_ID();

        for (NFA_Edge edges : nfa.getNFA_Edges()){
            if (edges.getFirst_id()==0||edges.getSecond_id()==0){
                edges.setFirst_id(start);
            }else if(edges.getSecond_id()==1||edges.getFirst_id()==1){
                edges.setSecond_id(end);
            }
            NFA_Edges.add(edges);
        }

        if(c=='*'||c=='+'){
            if(c=='*') {
                NFA_Edges.add(new NFA_Edge(0, 1, 'ε'));
            }
            NFA_Edges.add(new NFA_Edge(0,start,'ε'));
            NFA_Edges.add(new NFA_Edge(end,start,'ε'));
            NFA_Edges.add(new NFA_Edge(end,1,'ε'));
        }else if(c=='?'){
            NFA_Edges.add(new NFA_Edge(0, 1, 'ε'));
            NFA_Edges.add(new NFA_Edge(0,start,'ε'));
            NFA_Edges.add(new NFA_Edge(end,1,'ε'));
        }
    }

    public NFA(NFA nfa2, NFA nfa1, char c) {
        if(c=='|') {
            changeStartEnd(StaticVal.getNFA_ID(),StaticVal.getNFA_ID(),nfa1);
            changeStartEnd(StaticVal.getNFA_ID(),StaticVal.getNFA_ID(),nfa2);
        }
        else if(c=='▪'){
            int mid = StaticVal.getNFA_ID();
            copyLast(nfa1,0,mid);
            copyLast(nfa2,mid,1);

        }
    }

    private void copyLast(NFA nfa,int start,int end){
        for (NFA_Edge edges : nfa.getNFA_Edges()) {
            if (edges.getFirst_id() == 0 || edges.getSecond_id() == 0) {
                edges.setFirst_id(start);
            }
            if (edges.getSecond_id() == 1 || edges.getFirst_id() == 1) {
                edges.setSecond_id(end);
            }
            NFA_Edges.add(edges);
        }
    }


    private void changeStartEnd(int start,int end,NFA nfa){
        copyLast(nfa,start,end);
        NFA_Edges.add(new NFA_Edge(0,start,'ε'));
        NFA_Edges.add(new NFA_Edge(end,1,'ε'));
    }

    public List<NFA_Edge> getNFA_Edges() {
        return NFA_Edges;
    }


    /**
     * 当读到的是一个操作数的时候
     * 生成初始的NFA
     * */
    public NFA(char c){
        NFA_Edges.add(new NFA_Edge(0,1,c));
    }
}
