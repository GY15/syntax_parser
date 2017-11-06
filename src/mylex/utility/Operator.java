package mylex.utility;

import java.util.HashMap;

/**
 * Created by 61990 on 2017/10/27.
 * 记录各个运算符的优先级，用于栈的后缀
 */
public class Operator {
    private static HashMap<Character,Integer> priority;

    public static HashMap<Character,Integer> getPriority(){
        if (priority==null){
            priority=new HashMap<>();
            priority.put('+',8);
            priority.put('*',8);
            priority.put('(',1);
            priority.put('?',8);
            priority.put('▪',3);
            priority.put('|',5);
        }
        return priority;
    }
}
