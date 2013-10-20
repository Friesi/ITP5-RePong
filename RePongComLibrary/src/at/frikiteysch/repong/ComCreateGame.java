package at.frikiteysch.repong;

import java.io.Serializable;

public class ComCreateGame implements Serializable {

	private static final long serialVersionUID = 2L;
	
	private int creatorId;
	private int maxPlayerCount;
	private String gameName;
	
	public int getCreatorId() {
		return creatorId;
	}
	public void setCreatorId(int creatorId) {
		this.creatorId = creatorId;
	}
	public int getMaxPlayerCount() {
		return maxPlayerCount;
	}
	public void setMaxPlayerCount(int maxPlayerCount) {
		this.maxPlayerCount = maxPlayerCount;
	}
	public String getGameName() {
		return gameName;
	}
	public void setGameName(String gameName) {
		this.gameName = gameName;
	}
}