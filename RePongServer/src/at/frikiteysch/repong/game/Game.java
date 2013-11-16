package at.frikiteysch.repong.game;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
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
	private Boolean gameStarted;
	private ConcurrentMap<Integer, PlayerInfo> playerList = new ConcurrentHashMap<Integer, PlayerInfo>();
	private static Logger LOGGER = Logger.getLogger(Game.class.getName());

	public Game(int gameId, int maxPlayers, String gameName, int creatorId) {
		this.gameId = gameId;
		this.maxPlayers = maxPlayers;
		this.gameName = gameName;
		this.creatorId = creatorId;
		this.gameStarted = false;
	}
	
	
	public void run() {
		LOGGER.info("Game started");
		
		// TODO: iwo eine while schleife einbauen
		
		if (gameStarted) {
			// TODO: Gameplay ....
			// return GameData
		}
		else {
			ComWaitInfo waitInfo = new ComWaitInfo();
			waitInfo.setCreatorId(creatorId);
			waitInfo.setMaxPlayerCount(maxPlayers);
			waitInfo.setGameId(gameId);
			
			Map<Integer, String> clientPlayerList = new HashMap<Integer, String>();
			int cnt = 0;
			
			for (Entry<Integer, PlayerInfo> entry : playerList.entrySet()) {	// ClientPlayerList befüllen
				clientPlayerList.put(cnt++, entry.getValue().getName());
			}
			
			waitInfo.setPlayerList(clientPlayerList);
			
			for (Entry<Integer, PlayerInfo> entry : playerList.entrySet()) {	// an alle Clients senden
				CommunicationCenter.sendComObjectToClient(entry.getValue().getSocket(), waitInfo);
			}
		}
    }
	
	public void addPlayer(int id, String name, Socket socket) {
		PlayerInfo playerInfo = new PlayerInfo();
		playerInfo.setName(name);
		playerInfo.setSocket(socket);
		playerList.put(id, playerInfo);
		
		Thread t = new Thread( new PlayerPackageListener(socket) );	// start new thread to wait for new packages from this player
		t.start();
	}
	
	public void removePlayer() {
		
	}
	
	public ConcurrentMap<Integer, PlayerInfo> getPlayerList() {
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
