package mylex.utility;

/**
 * Created by 61990 on 2017/10/28.
 */
public class Token {
    private String token;
    private String yylval;
    private String text;

    public String getToken() {
        return token;
    }

    public Token(Token_DFA DFA, String text){
        this.token = DFA.getToken();
        this.text = text;
        this.yylval = DFA.getYylval();
        if(StaticVal.getTokensNeedVal().contains(token)){
            this.yylval = text;

        }
    }

    public String getText() {
        return text;
    }

    public String getYylval() {
        return yylval;
    }
}
