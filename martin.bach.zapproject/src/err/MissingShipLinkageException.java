package err;

public class MissingShipLinkageException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public MissingShipLinkageException(String m) {
		super(m);
	}
	
	public MissingShipLinkageException() {
		super();
	}
}
