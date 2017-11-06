package mylex.process;

import mylex.exception.NotREsException;
import mylex.utility.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * Created by 61990 on 2017/10/25.
 * 通过输入的 Token_RE yylval RE postfix 的 list 生成 各个NFA
 */
public class NFA_collection {
    //保存处理过的expression
    private List<Token_RE> regularExpressions;

    public List<Token_NFA> getNFA_Expressions() {
        return NFA_Expressions;
    }

    private List<Token_NFA> NFA_Expressions;

    public NFA_collection(List<Token_RE> regularExpressions) {
        this.regularExpressions = regularExpressions;
        NFA_Expressions = new ArrayList<>();
    }

    /**
     *
     * 处理后缀表达式到NFA
     */
    public void handle_RE_to_NFA() throws NotREsException {
        for (int i = 0; i < regularExpressions.size(); i++) {
            try {
                Token_RE token_re = regularExpressions.get(i);
                NFA_Expressions.add(new Token_NFA(token_re,RE_to_NFA(token_re.getExpression())));
            } catch (Exception e) {
                e.printStackTrace();
                throw new NotREsException(i);
            }
        }
    }

    public NFA RE_to_NFA(String RE) throws Exception {
        Stack<NFA> stack = new Stack<>();
        for (int i = 0; i < RE.length(); i++) {
            NFA handle = null;
            if (RE.charAt(i) == '\\') {
                if (RE.charAt(i + 1) == 't') {
                    handle = new NFA('\t');
                } else if (RE.charAt(i + 1) == 'n') {
                    handle = new NFA('\n');
                } else {
                    handle = new NFA(RE.charAt(i + 1));
                }
                i++;
            } else if (StaticVal.isOperand(RE.charAt(i))||RE.charAt(i)==' ') {
                handle = new NFA(RE.charAt(i));
            } else {
                switch (RE.charAt(i)) {
                    case '*':
                        handle = new NFA(stack.pop(),'*');
                        break;
                    case '+':
                        handle = new NFA(stack.pop(),'+');
                        break;
                    case '|':
                        handle = new NFA(stack.pop(),stack.pop(),'|');
                        break;
                    case '?':
                        handle = new NFA(stack.pop(),'?');
                        break;
                    case '▪':
                        handle = new NFA(stack.pop(),stack.pop(),'▪');
                        break;
                    default:
                        throw new Exception();
                }
            }
            stack.push(handle);
        }
        if (stack.size()==1){
//            NFA a = stack.peek();
//            for (NFA_Edge s: a.getNFA_Edges()){
//                System.out.println(s.getFirst_id()+" "+ s.getSecond_id()+" " + s.getValue());
//            }
            return stack.pop();
        }else{
            throw new Exception();
        }
    }


}
