package myLL1.utility;

/**
 * Created by 61990 on 2017/11/7.
 */
public class PPT_item {
    Sign start;
    Sign end;
    Derivation derivation;

    public PPT_item(Sign start, Sign end, Derivation derivation) {
        this.start = start;
        this.end = end;
        this.derivation = derivation;
    }

    public Sign getStart() {
        return start;
    }

    public Sign getEnd() {
        return end;
    }

    public Derivation getDerivation() {
        return derivation;
    }
}
