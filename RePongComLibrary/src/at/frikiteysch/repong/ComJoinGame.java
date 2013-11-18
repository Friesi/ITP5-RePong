package at.frikiteysch.repong;

import java.io.Serializable;

public class ComJoinGame implements RequiresLoggedInUserObject, Serializable {

	private static final long serialVersionUID = 3L;
	
	private int gameId;
	private int userId;
	
	public int getGameId() {
		return gameId;
	}
	public void setGameId(int gameId) {
		this.gameId = gameId;
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
