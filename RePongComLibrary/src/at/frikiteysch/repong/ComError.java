package at.frikiteysch.repong;

import java.io.Serializable;

import at.frikiteysch.repong.defines.RePongDefines;

public class ComError  implements Serializable {

	private static final long serialVersionUID = 13L;
	
	private int errorCode;
	private String error;
	
	public ComError()
	{
		this.errorCode = RePongDefines.Error.GENERAL_ERROR.getErrorCode();
		this.error = RePongDefines.Error.GENERAL_ERROR.getErrorMsg();
	}
	
	public ComError(RePongDefines.Error error)
	{
		this.errorCode = error.getErrorCode();
		this.error = error.getErrorMsg();
	}
	
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
	
	public String printError()
	{
		return "Error: Code<" + errorCode + "> Msg<" + error + ">";
	}
}
