package mylex.exception;

public class NotTokenException extends Exception {
	private static final long serialVersionUID = 1L;
	int line;
	public NotTokenException(int num){
		this.line=num+1;
	}
	@Override
	public String getMessage() {
		return ("输入的第"+line+"行文件有问题。请重新输入");
	}

}
