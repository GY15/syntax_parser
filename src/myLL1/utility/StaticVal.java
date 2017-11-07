package myLL1.utility;

import java.util.HashMap;
import java.util.List;

/**
 * Created by 61990 on 2017/11/7.
 */
public class StaticVal {
    /**
     * 保存之前是否存在过相同的String表达式，防止重复
     *
     * */
    private static HashMap<String,Character> map = new HashMap<>();

    public static void init(){
        map.put("ε",'ε');
    }

    public static Sign firstSign;

    //返回目前sign的ID的号码
    private static char id = 'A';
    public static char getID(String str){
        if(map.get(str)==null) {
            map.put(str,id);
            return id++;
        }else{
            return map.get(str);
        }
    }

    public static Sign getNullSign(){
        return new Sign('ε',"ε",true);
    }

    //返回目前表达式的号码
    private static int exp_id = 0;
    public static int getExp_id_ID(){
        return exp_id++;
    }
}
