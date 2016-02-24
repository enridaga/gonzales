package gonzales.xml;

public final class GonzoException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public GonzoException() {
	}

	public GonzoException(String msg) {
		super(msg);
	}

	public GonzoException(Throwable cause) {
		super(cause);
	}

	public GonzoException(String msg, Throwable cause) {
		super(msg, cause);
	}

}
