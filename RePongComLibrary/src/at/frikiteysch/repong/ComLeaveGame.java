package at.frikiteysch.repong;

import java.io.Serializable;

public class ComLeaveGame implements RequiresLoggedInUserObject, Serializable {

	private static final long serialVersionUID = 7L;
	
	private int userId;

	@Override
	public int getUserId() {
		return userId;
	}

	@Override
	public void setUserId(int userId) {
		this.userId = userId;
	}
}
