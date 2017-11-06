package mylex.utility;

/**
 * Created by 61990 on 2017/10/27.
 */
public class DFA_Edge {
    private DFA DFA_first;
    int firstID;

    public void setFirstID(int firstID) {
        this.firstID = firstID;
    }

    public void setSecondID(int secondID) {
        this.secondID = secondID;
    }

    @Override
    public boolean equals(Object obj) {
        DFA_Edge s = (DFA_Edge)obj;
        return (s.secondID==this.secondID)&&(s.firstID==this.firstID)&&(s.getEdgeChar()==this.getEdgeChar());
    }

    private DFA DFA_second;
    int secondID;
    private char edge;

    public DFA getDFA_first() {
        return DFA_first;
    }

    public DFA getDFA_second() {
        return DFA_second;
    }

    public char getEdgeChar() {
        return edge;
    }

    public int getFirstID() {
        return firstID;
    }

    public int getSecondID() {
        return secondID;
    }

    public DFA_Edge(DFA DFA_first, DFA DFA_second, char edge) {
        this.DFA_first = DFA_first;
        firstID = DFA_first.getID();
        this.DFA_second = DFA_second;
        secondID = DFA_second.getID();

        this.edge = edge;
    }
}
