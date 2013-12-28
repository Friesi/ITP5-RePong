package at.frikiteysch.repong;

import java.io.Serializable;

public class ComStartGame  implements RequiresLoggedInUserObject, Serializable {

	private static final long serialVersionUID = 8L;
	
	private int userId;
	private int gameId;

	@Override
	public int getUserId() {
		return userId;
	}

	@Override
	public void setUserId(int userId) {
		this.userId = userId;
	}
	
	public int getGameId() {
		return gameId;
	}
	
	public void setGameId(int gameId) {
		this.gameId = gameId;
	}
}
