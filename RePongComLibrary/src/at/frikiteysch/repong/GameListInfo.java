package at.frikiteysch.repong;

import java.io.Serializable;


public class GameListInfo implements Serializable{
	
	private static final long serialVersionUID = 11L;
	private int curPlayerCount;
	private int gameId;
	private int maxPlayerCount;
	private String creatorName;
	private String gameName;
	
	public int getCurPlayerCount() {
		return curPlayerCount;
	}
	public void setCurPlayerCount(int curPlayerCount) {
		this.curPlayerCount = curPlayerCount;
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
	public String getCreatorName() {
		return creatorName;
	}
	public void setCreatorName(String creatorName) {
		this.creatorName = creatorName;
	}
	public String getGameName() {
		return gameName;
	}
	public void setGameName(String gameName) {
		this.gameName = gameName;
	}
}
