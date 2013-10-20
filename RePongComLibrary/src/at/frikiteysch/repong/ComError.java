package at.frikiteysch.repong;

import java.io.Serializable;

public class ComError  implements Serializable {

	private static final long serialVersionUID = 13L;
	
	private int errorCode;
	private String error;
	
	public int getErrorCode() {
		return errorCode;
	}
	public void setErrorCode(int errorCode) {
		this.errorCode = errorCode;
	}
	public String getError() {
		return error;
	}
	public void setError(String error) {
		this.error = error;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}
