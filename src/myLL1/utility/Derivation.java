package myLL1.utility;

import java.util.List;

/**
 * Created by 61990 on 2017/11/7.
 */
public class Derivation {
    private int ID;
    private Sign head;
    private List<Sign> tail;

    public Derivation(int ID, Sign head, List<Sign> tail) {
        this.ID = ID;
        this.head = head;
        this.tail = tail;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public void setHead(Sign head) {
        this.head = head;
    }

    public void setTail(List<Sign> tail) {
        this.tail = tail;
    }

    public int getID() {

        return ID;
    }

    public Sign getHead() {
        return head;
    }

    public List<Sign> getTail() {
        return tail;
    }
}
