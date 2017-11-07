package myLL1.utility;

/**
 * Created by 61990 on 2017/11/7.
 */
public class Sign {
    private char ID;
    private String val;
    private boolean isTerminal;

    public Sign(char ID, String val, boolean isTerminal) {
        this.ID = ID;
        this.val = val;
        this.isTerminal = isTerminal;
    }

    @Override
    public boolean equals(Object obj) {
        return ((Sign) obj).ID == this.ID;
    }

    public char getID() {
        return ID;
    }

    public String getVal() {
        return val;
    }

    public boolean isTerminal() {
        return isTerminal;
    }
}
