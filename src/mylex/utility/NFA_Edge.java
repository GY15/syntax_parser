package mylex.utility;

/**
 * Created by 61990 on 2017/10/27.
 */
public class NFA_Edge {
    private int first_id;
    private int second_id;
    private char value;

    public int getFirst_id() {
        return first_id;
    }

    public void setFirst_id(int first_id) {
        this.first_id = first_id;
    }

    public int getSecond_id() {
        return second_id;
    }

    public void setSecond_id(int second_id) {
        this.second_id = second_id;
    }

    public char getValue() {
        return value;
    }

    public void setValue(char value) {
        this.value = value;
    }

    public NFA_Edge(int first,int second,char value){
        first_id = first;
        second_id = second;
        this.value = value ;
    }
}
