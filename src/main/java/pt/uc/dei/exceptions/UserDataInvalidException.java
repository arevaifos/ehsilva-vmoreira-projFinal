package pt.uc.dei.exceptions;

public class UserDataInvalidException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1822380576326789428L;

	private String errorCode = null;

	public UserDataInvalidException() {
		super();
	}

	public UserDataInvalidException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public UserDataInvalidException(String message, Throwable cause) {
		super(message, cause);
	}

	public UserDataInvalidException(String message) {
		super(message);
	}

	public UserDataInvalidException(String errorCode, String message) {
		super(message);
		this.errorCode = errorCode;
	}

	public UserDataInvalidException(Throwable cause) {
		super(cause);
	}

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

}
