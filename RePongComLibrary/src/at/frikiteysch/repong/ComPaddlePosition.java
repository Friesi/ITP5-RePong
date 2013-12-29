package at.frikiteysch.repong;

import java.io.Serializable;

public class ComPaddlePosition  implements RequiresLoggedInUserObject, Serializable {

	private static final long serialVersionUID = 9L;
	
	private int positionNorm;
	private int userId;
	private int gameId;

	public int getPositionNorm() {
		return positionNorm;
	}

	public void setPositionNorm(int positionNorm) {
		this.positionNorm = positionNorm;
	}

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
