package at.frikiteysch.repong.game;

public class Game implements Runnable {
	
	private int gameId;
	private int maxPlayers;
	private String gameName;
	private Boolean gameStarted;

	public Game(int gameId, int maxPlayers, String gameName) {
		this.gameId = gameId;
		this.maxPlayers = maxPlayers;
		this.gameName = gameName;
	}
	
	
	public void run() {
    	//Code
		
		
		
		
		
		
    }
	
	
	public int getGameId() {
		return gameId;
	}

	public void setGameId(int gameId) {
		this.gameId = gameId;
	}

	public int getMaxPlayers() {
		return maxPlayers;
	}

	public void setMaxPlayers(int maxPlayers) {
		this.maxPlayers = maxPlayers;
	}

	public String getGameName() {
		return gameName;
	}

	public void setGameName(String gameName) {
		this.gameName = gameName;
	}

	public Boolean getGameStarted() {
		return gameStarted;
	}

	public void setGameStarted(Boolean gameStarted) {
		this.gameStarted = gameStarted;
	}
}
