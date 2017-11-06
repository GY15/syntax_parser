package mylex.utility;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 61990 on 2017/10/26.
 * 保存需要从文本里提取yylval 的token
 *
 */
public class StaticVal {
    /**
     *
     * 保存需要从文本里提取yylval 的token
     */
    public static List<String> token;

    public static List<String> getTokensNeedVal() {
        if(token ==null){
            token = new ArrayList<>();
        }
            return token;
    }
    public static void add(String token){
        getTokensNeedVal().add(token);
    }

    /**
     *保存需要加连接符的特殊字符
     */
    public static char[] sign = new char[]{'=','>','<','!'};
    public static boolean isReserved(char c){
        for (int i =0 ;i<sign.length;i++){
            if (c==sign[i]){
                return true;
            }
        }
        return false;
    }
    //RE中需要连接符单目运算符的符号
    public static char[] re_sign = new char[]{'+','?','*'};
    public static boolean isSign(char c){
        for (int i =0 ;i<re_sign.length;i++){
            if (c==re_sign[i]){
                return true;
            }
        }
        return false;
    }
    //RE中双目运算符的符号
    public static char[] re_double_sign = new char[]{'|','▪'};
    public static boolean isDoubleSign(char c){
        for (int i =0 ;i<re_double_sign.length;i++){
            if (c==re_double_sign[i]){
                return true;
            }
        }
        return false;
    }
    //判读是否是一个操作数
    public static boolean isOperand(char c){
        if ((c<='z'&&c>='a')||(c<='Z'&&c>='A')||(c<='9'&&c>='0')|| isReserved(c)){
            return true;
        }
        return false;
    }
    //返回目前的NFA ID号码
    private static int NFA_id = 2;
    public static int getNFA_ID(){
        return NFA_id++;
    }

    public static void setDFA_id(int DFA_id) {
        StaticVal.DFA_id = DFA_id;
    }

    //返回目前的DFA ID号码
    private static int DFA_id = 0;
    public static int getDFA_ID(){
        return DFA_id++;
    }

}
