package mylex.utility;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 61990 on 2017/10/27.
 */
public class DFA {
    private List<Integer> list;
    private boolean isTerminal;

    public boolean isTerminal() {
        return isTerminal;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    private int ID;
    /**
     * 记录DFA包含的所有NFA状态和标志位ID
     * */
    public DFA(List<Integer> list, int ID) {
        //是否包含终止状态1
        if(list.contains(1)){
            isTerminal =true;
        }else{
            isTerminal = false;
        }
        this.list = list;
        this.ID = ID;
    }

    public List<Integer> getList() {
        return list;
    }

    public int getID() {
        return ID;
    }

}
