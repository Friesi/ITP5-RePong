package at.frikiteysch.repong;

import java.io.Serializable;

public class Herbert implements RequiresLoggedInUserObject, Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 7588504591693333977L;
	
	private int playerId;

	public int getPlayerId() {
		return playerId;
	}

	public void setPlayerId(int playerId) {
		this.playerId = playerId;
	}

	@Override
	public int getUserId() {
		return playerId;
	}

	@Override
	public void setUserId(int userId) {
		this.playerId = userId;
	}
}
