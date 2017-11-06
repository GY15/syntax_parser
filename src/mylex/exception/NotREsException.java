package mylex.exception;

public class NotREsException extends Exception {
	private static final long serialVersionUID = 1L;
	int line;
	public NotREsException(int num){
		this.line=num+1;
	}
	@Override
	public String getMessage() {
		return ("输入的第"+line+"行正则表达式不合法。请重新输入");
	}

}
