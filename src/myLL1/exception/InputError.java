package myLL1.exception;

/**
 * Created by 61990 on 2017/11/7.
 */
public class InputError extends Exception {
    private static final long serialVersionUID = 1L;

    @Override
    public String getMessage() {
        return "输入的文法和文本不匹配";
    }
}

