package at.frikiteysch.repong.game;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import at.frikiteysch.repong.ComLeaveGame;
import at.frikiteysch.repong.ComWaitInfo;
import at.frikiteysch.repong.IncomingPackageSwitch;
import at.frikiteysch.repong.communication.CommunicationCenter;
import at.frikiteysch.repong.players.PlayerInfo;
import at.frikiteysch.repong.players.TerminatorThread;

public class Game implements Runnable {
	
	private int gameId;	
	private int creatorId;
	private String creatorName;
	private int maxPlayers;
	private String gameName;
	private Boolean gameStarted, gameEnd, gameTerminate;
	private ConcurrentMap<Integer, String> playerList = new ConcurrentHashMap<Integer, String>();
	private static Logger LOGGER = Logger.getLogger(Game.class.getName());

	public Game(int gameId, int maxPlayers, String gameName, int creatorId, String creatorName){
		this (gameId, maxPlayers, gameName,creatorId);
		this.creatorName=creatorName;
		
	}
	
	public Game(int gameId, int maxPlayers, String gameName, int creatorId) {
		this.gameId = gameId;
		this.maxPlayers = maxPlayers;
		this.gameName = gameName;
		this.creatorId = creatorId;
		this.gameStarted = false;	// game is running, calculation is processing
		this.gameEnd = false;		// game has finished normally
		this.gameTerminate = false;	// game has terminated because all users (or the creator in waiting room) have/s left the game
	}
	
	
	public void run() {
		LOGGER.info("Game with id " + gameId + " started!");
		
		while(!gameEnd && !gameTerminate) {	// till the game is finished
			if (gameStarted) {
				// TODO: Gameplay ....
				
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
		if (gameEnd) {
			// TODO: show score / winning table
		}
    }
	
	public void getComWaitInfo(Socket socket) {
		ComWaitInfo waitInfo = new ComWaitInfo();
		waitInfo.setCreatorId(creatorId);
		waitInfo.setMaxPlayerCount(maxPlayers);
		waitInfo.setGameId(gameId);
		
		Map<Integer, String> clientPlayerList = new HashMap<Integer, String>();
		int cnt = 0;
		
		for (Entry<Integer, String> entry : playerList.entrySet()) {	// fill ClientPlayerList
			clientPlayerList.put(cnt++, entry.getValue());
		}
		
		waitInfo.setPlayerList(clientPlayerList);
		
		CommunicationCenter.sendComObjectToClient(socket, waitInfo);
	}
	
	public void addPlayer(int playerId, String name, Socket socket) {	// TODO: synchronisieren??
		if (playerList.size() < maxPlayers)
			playerList.put(playerId, name);
	}
	
	public Boolean removePlayer(int playerId) {
		if (playerId == creatorId) {	// creator left game
			gameTerminate = true;
			return true;
		}
		
		if (playerList.containsKey(playerId))
			playerList.remove(playerId);
		
		return false;
	}
	
	public ConcurrentMap<Integer, String> getPlayerList() {
    	return playerList;
    }
	
	public int getGameId() {
		return gameId;
	}

	public void setGameId(int gameId) {
		this.gameId = gameId;
	}
	
	public int getCreatorId() {
		return creatorId;
	}

	public void setCreatorId(int creatorId) {
		this.creatorId = creatorId;
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
	
	public String getCreatorName() {
		return creatorName;
	}

	public void setCreatorName(String creatorName) {
		this.creatorName = creatorName;
	}
}
