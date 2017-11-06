package mylex.utility;

import java.util.List;

/**
 * Created by 61990 on 2017/10/27.
 */
public class Token_NFA {
    private String token;
    private String yylval;
    private NFA NFA;

    /**
     * 保存生成的 NFA 和从RE得来的 token
     */
    public Token_NFA(Token_RE regularExpressions, NFA nfa) {
        this.token = regularExpressions.getToken();
        this.yylval = regularExpressions.getYylyal();
        this.NFA = nfa;
    }

    public NFA getNFA() {
        return NFA;
    }

    public String getToken() {
        return token;
    }

    public String getYylval() {
        return yylval;
    }
}
