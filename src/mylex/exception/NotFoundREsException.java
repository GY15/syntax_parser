package mylex.exception;

public class NotFoundREsException extends Exception {
	private static final long serialVersionUID = 1L;

	@Override
	public String getMessage() {
		return "文件格式不符合要求";
	}

}
