package at.frikiteysch.repong;

import java.io.Serializable;

public class ComLogin implements RequiresLoggedInUserObject, Serializable {

	private static final long serialVersionUID = 1L;
	
	private int userId;
	private String userName;
	
	@Override
	public int getUserId() {
		return userId;
	}
	
	@Override
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
}
