package at.frikiteysch.repong;

import java.io.Serializable;

public class ComStartGame  implements RequiresLoggedInUserObject, Serializable {

	private static final long serialVersionUID = 8L;
	
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
