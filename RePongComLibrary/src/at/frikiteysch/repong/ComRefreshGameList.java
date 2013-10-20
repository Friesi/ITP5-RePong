package at.frikiteysch.repong;

import java.io.Serializable;

public class ComRefreshGameList implements Serializable {

	private static final long serialVersionUID = 5L;
	
	private int userId;

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}
}
