package at.frikiteysch.repong;

import java.io.Serializable;

public class ComRefreshGameList implements RequiresLoggedInUserObject, Serializable {

	private static final long serialVersionUID = 5L;
	
	private int userId;

	public ComRefreshGameList(){
		
	}
	
	@Override
	public int getUserId() {
		return userId;
	}

	@Override
	public void setUserId(int userId) {
		this.userId = userId;
	}
}
