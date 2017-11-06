package mylex.utility;

import java.util.List;

/**
 * Created by 61990 on 2017/10/27.
 */
public class Token_DFA {
    private String token;
    private String yylval;

    public void setDfa_edges(List<DFA_Edge> dfa_edges) {
        this.dfa_edges = dfa_edges;
    }

    private List<DFA_Edge> dfa_edges;
    /**
     * 保存生成的 DFA 和从NFA得来的 token
     */
    public Token_DFA(Token_NFA NFA_Expressions, List<DFA_Edge> dfa_edges) {
        this.token = NFA_Expressions.getToken();
        this.yylval = NFA_Expressions.getYylval();
        this.dfa_edges = dfa_edges;
    }

    public String getToken() {
        return token;
    }

    public String getYylval() {
        return yylval;
    }

    public List<DFA_Edge> getDfa_edges() {
        return dfa_edges;
    }
}
