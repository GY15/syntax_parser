package mylex.exception;

public class WrongSort extends Exception {
	private static final long serialVersionUID = 1L;

	@Override
	public String getMessage() {
		return "\"-\"两边的值不匹配";
	}

}
