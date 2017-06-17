package err;

public class InitializationTimingException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public InitializationTimingException(String m) {
		super(m);
	}
	
	public InitializationTimingException() {
		super();
	}
}
