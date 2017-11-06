package mylex.process;

import mylex.exception.NotFoundREsException;
import mylex.exception.NotREsException;
import mylex.exception.WrongSort;
import mylex.utility.Operator;
import mylex.utility.StaticVal;
import mylex.utility.Token_RE;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * Created by 61990 on 2017/10/25.
 * <p>
 * 用于读取.l 文件 读出
 * 并初始化
 * 并保留各个token
 */
public class RE_collection {
    //文件路径
    private String filePath;
    /**
     * 用来保存所有的正则表达式  token yylval re
     */
    private List<Token_RE> expressions;


    public RE_collection(String path) {
        this.filePath = path;
        expressions = new ArrayList<>();
    }

    public List<Token_RE> getExpressions() {
        return expressions;
    }

    /**
     * 从文件中读取RE的信息

     */
    public void createREs() throws IOException, NotFoundREsException {
        FileReader fr = null;
        File file = new File(filePath);
        fr = new FileReader(file);

        @SuppressWarnings("resource")
        BufferedReader br = new BufferedReader(fr);
        String temp = null;
        while ((temp = br.readLine()) != null) {
            if (temp.trim().equals("") || temp.trim().equals("%%")) {
                continue;
            }

            String str = temp.split(" ")[0];

            if (temp.length() < (str.length() + 1)) {
                throw new NotFoundREsException();
            } else {
                expressions.add(new Token_RE(str, temp.substring(str.length() + 1).trim()));
            }
        }

//        System.out.println(StaticVal.getTokensNeedVal().size());
    }

    public String deleteBig(String expr){
        for (int i = 0; i< expr.length();i++){
            if (expr.charAt(i)=='\\'){
                i++;
                continue;
            }
            if (expr.charAt(i)=='{') {
                String first = "", second = "";
                boolean isFirst = true;
                int last = i,start = i;
                for (int j = i + 1; j < expr.length(); j++) {
                    if (expr.charAt(j) == ',') {
                        isFirst = false;
                    } else if (expr.charAt(j) == '}') {
                        if (isFirst) {
                            second = first;
                        }
                        last = j;
                        break;
                    } else {
                        if (isFirst) {
                            first += expr.charAt(j);
                        } else {
                            second += expr.charAt(j);
                        }
                    }
                }
                int num1 = Integer.parseInt(first.trim());
                int num2 = Integer.parseInt(second.trim());

                String str = "";
                String loop="";
                if (expr.charAt(i - 1) == ')'){
                    int num = 0;
                    for (int j = i-1;j>=0;j--){
                        if(expr.charAt(j)==')'){
                            num++;
                        }
                        if(expr.charAt(j)=='('){
                            num--;
                            if(num==0){
                                loop = expr.substring(j,i);
                                start = j+1;
                                break;
                            }
                        }
                    }

                }else{
                    loop = "" + expr.charAt(i - 1);
                }
                    for (int k = num1; k <= num2; k++) {
                        for (int s = 0; s < k; s++) {
                            str += loop;
                        }
                        if (k != num2) {
                            str += "|";
                        }
                    }

                if(last<expr.length()-1) {
                    expr = expr.substring(0, start - 1) + "("+str +")"+ expr.substring(last + 1);
                }else{
                    expr = expr.substring(0, start - 1) +  "("+str +")";
                }
            }
        }
        return expr;
    }
    /**
     * 加上连接符号
     * 再生成后缀表达式
     */
    public void postfixERs() throws WrongSort, NotREsException {
        for (int num = 0; num < expressions.size(); num++) {
            Token_RE token = expressions.get(num);
            String expr = format(token.getExpression());
            try {
                //去除大括号
                expr = deleteBig(expr);
                expr = addConnector(expr);
                expr = postfix(expr);
            } catch (Exception e) {
                throw new NotREsException(num);
            }
            expressions.get(num).setExpression(expr);
        }
    }


    /**
     * 中缀变后缀
     */
    public String postfix(String expr) throws Exception {
//        System.out.println(expr);
        Stack<Character> stack = new Stack<>();
        StringBuffer postfix = new StringBuffer();
        for (int i = 0; i < expr.length(); i++) {
            char c = expr.charAt(i);
            if (c == '\\') {
                postfix.append(c);
                postfix.append(expr.charAt(i+1));
                i++;
            }
            else if (StaticVal.isOperand(c)||c == ' ') {
                postfix.append(String.valueOf(c));
            } else if (StaticVal.isDoubleSign(c) || StaticVal.isSign(c)) {
                while (stack.size() != 0) {
                    char top = stack.peek();
                    if (Operator.getPriority().get(top) >= Operator.getPriority().get(c)) {
                        postfix.append(stack.pop());
                    } else {
                        break;
                    }
                }
                stack.push(c);
            } else if (c == '(') {
                stack.push(c);
            } else if (c == ')') {
                while (true) {
                    char theChar = stack.pop();
                    if (theChar != '(') {
                        postfix.append(theChar);
                    } else {
                        break;
                    }
                }
            }
        }
        while (stack.size() != 0) {
            postfix.append(stack.pop());
        }
//        System.out.println(postfix);
        return postfix.toString();
    }

    /**
     * 去掉通配符 .
     * 去掉中括号 用（|）代替
     */
    private String format(String expression) throws WrongSort {
        for (int i = 0;i<expression.length();i++){
            if(expression.charAt(i)=='\\'){
                i++;
                continue;
            }else {
                if (expression.charAt(i)=='.') {
                    if (i == expression.length()-1) {
                        expression = expression.substring(0, i) + "[a-zA-Z0-9]" ;
                    } else {
                        expression = expression.substring(0, i) + "[a-zA-Z0-9]" + expression.substring(i + 1);
                    }
                }
            }
        }
        for (int i = 0; i < expression.length(); i++) {
            if (expression.charAt(i) == '[') {
                for (int j = i + 1; j < expression.length(); j++) {
                    if (expression.charAt(j) == ']') {
                        String mid = expression.substring(i + 1, j);
                        mid = addOR(mid);
                        expression = expression.substring(0, i) + mid + expression.substring(j + 1);
                        i = 0;
                        break;
                    }
                }
            }

        }

        return expression;
    }

    /**
     * 加上或，去掉中括号
     */
    private String addOR(String str) throws WrongSort {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("(");
        for (int i = 0; i < str.length() - 1; i++) {
            if (str.charAt(i) == '\\') {
                stringBuffer.append("\\");
                continue;
            }
            stringBuffer.append(str.substring(i, i + 1) + "|");

            if (str.charAt(i + 1) == '-') {
                if (str.length() > i + 2) {
                    if (((str.charAt(i) <= 'Z' && str.charAt(i) >= 'A') && (str.charAt(i + 2) <= 'Z' && str.charAt(i + 2) >= 'A')) ||
                            ((str.charAt(i) <= 'z' && str.charAt(i) >= 'a') && (str.charAt(i + 2) <= 'z' && str.charAt(i + 2) >= 'a')) ||
                            ((str.charAt(i) <= '9' && str.charAt(i) >= '0') && (str.charAt(i + 2) <= '9' && str.charAt(i + 2) >= '0'))) {
                        if (str.charAt(i) >= str.charAt(i + 2)) {
                            throw new WrongSort();
                        }

                        char begin = str.charAt(i);
                        char end = str.charAt(i + 2);
                        for (char j = (char) (begin + 1); j < end; j++) {
                            stringBuffer.append(String.valueOf(j) + "|");
                        }
                        i++;

                    }
                }
            }
        }

        stringBuffer.append(str.substring(str.length() - 1) + ")");
        return stringBuffer.toString();
    }

    /**
     * 加上连接符号
     */
    private String addConnector(String expression) {

        for (int i = 1; i < expression.length(); i++) {
            if (isAdd(expression.charAt(i - 1), expression.charAt(i))) {
                expression = expression.substring(0, i) + "▪" + expression.substring(i);
                i++;
            }
        }
//        System.out.println(expression);
        return expression;
    }

    /**
     * 判断两个字符之间是否加▪
     */
    private boolean isAdd(char ahead, char tail) {
        if (ahead == '\\'){
            return false;
        }
        if (tail == '(') {
            if (ahead == '(' || StaticVal.isDoubleSign(ahead)) {
                return false;
            } else
                return true;
        } else {
            return isEnd(ahead) && StaticVal.isOperand(tail);
        }
    }

    /**
     * 判断字符是否是要连接的
     */
    private boolean isEnd(char c) {
        if ((c <= 'z' && c >= 'a') || (c <= 'Z' && c >= 'A') || (c <= 9 && c >= 0) || StaticVal.isReserved(c) || StaticVal.isSign(c) || c == ')') {
            return true;
        }
        return false;
    }
}
