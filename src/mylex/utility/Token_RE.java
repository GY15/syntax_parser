package mylex.utility;

/**
 * Created by 61990 on 2017/10/26.
 */
public class Token_RE {
    private String token;
    private String yylval;
    private String expression;

    /**
     * 初始化队列，将token等分开
     * */
    public Token_RE(String token, String expression) {
        this.expression = expression;
        try {
            String array[] = token.split("/");
            if(array.length==1 && array[0].length()!=token.length()){
                this.setToken(array[0]);
                this.setYylyal(null);
                StaticVal.add(array[0]);
            }else if(array.length==1){
                this.setToken(array[0]);
                this.setYylyal(null);
            }else{
                this.setToken(array[0]);
                this.setYylyal(array[1]);
            }
        }catch (Exception e ){
            this.setToken(token);
        }

    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getYylyal() {
        return yylval;
    }

    public void setYylyal(String yylval) {
        this.yylval = yylval;
    }

    public String getExpression() {
        return expression;
    }

    public void setExpression(String expression) {
        this.expression = expression;
    }
}