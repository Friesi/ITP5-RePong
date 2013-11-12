package at.frikiteysch.repong;

import java.io.Serializable;
import java.util.Map;

public class ComWaitInfo  implements Serializable {

	private static final long serialVersionUID = 4L;
	
	private int creatorId;
	private int gameId;
	private int maxPlayerCount;
	private Map<Integer, String> playerList;
	
	public int getCreatorId() {
		return creatorId;
	}
	public void setCreatorId(int creatorId) {
		this.creatorId = creatorId;
	}
	public int getGameId() {
		return gameId;
	}
	public void setGameId(int gameId) {
		this.gameId = gameId;
	}
	public int getMaxPlayerCount() {
		return maxPlayerCount;
	}
	public void setMaxPlayerCount(int maxPlayerCount) {
		this.maxPlayerCount = maxPlayerCount;
	}
	public Map<Integer, String> getPlayerList() {
		return playerList;
	}
	public void setPlayerList(Map<Integer, String> playerList) {
		this.playerList = playerList;
	}
}
