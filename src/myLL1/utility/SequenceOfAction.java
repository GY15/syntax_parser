package myLL1.utility;

import mylex.utility.Token;

/**
 * Created by 61990 on 2017/11/8.
 */
public class SequenceOfAction {
    String action;
    Derivation derivation;
    Token input;

    //当动作为输出时使用
    public SequenceOfAction(String action, Derivation derivation) {
        this.action = action;
        this.derivation = derivation;
        input=null;
    }

    //当动作为匹配时使用
    public SequenceOfAction(String action, Token input) {
        this.action = action;
        this.input = input;
        derivation =null;
    }

    @Override
    public String toString() {
        String output= action;
        if (derivation==null){
            String val;
            output +=" "+input.getToken();
            if (input.getYylval()!=null) {
                val = "(" + input.getYylval() + ")";
                output += " " + val;
            }
        }else{
            String val = derivation.getHead().getVal()+" -> ";
            String tail ="";
            for (Sign sign : derivation.getTail()){
                tail+=" "+sign.getVal();
            }
            output+=" "+val+tail;
        }
        return output;
    }
}
