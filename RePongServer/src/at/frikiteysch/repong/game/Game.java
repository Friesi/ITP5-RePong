package at.frikiteysch.repong.game;

import java.awt.Color;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import at.frikiteysch.repong.ComGameData;
import at.frikiteysch.repong.ComWaitInfo;
import at.frikiteysch.repong.Player;
import at.frikiteysch.repong.communication.CommunicationCenter;
import at.frikiteysch.repong.defines.RePongDefines;
import at.frikiteysch.repong.defines.RePongDefines.PaddleOrientation;

public class Game implements Runnable {
	
	private int gameId;	
	private int creatorId;
	private String creatorName;
	private int maxPlayers;
	private String gameName;
	private Boolean gameStarted, gameEnd, gameTerminate;
	private ConcurrentMap<Integer, String> playerList = new ConcurrentHashMap<Integer, String>();
	private ArrayList<Player> playerInGame = new ArrayList<Player>(); // only available if game has started
	private GameDataCalculator gamePlay; // holds the data for the game itself
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
				
				gamePlay.recalculate();
				
				if (gamePlay.gameFinished()) // end game if finished
					gameEnd = true;
				
				try {
					Thread.sleep(25);
				} catch (InterruptedException e) {
					LOGGER.log(Level.SEVERE, "Interrupted Thread in game with Id<" + gameId + ">", e);
				}
			}
		}
		
		if (gameEnd) {
			LOGGER.info("game thread ended");
			// TODO: show score / winning table
		}
		if (gameTerminate)
		{
			LOGGER.info("game thread terminated");
		}
		
    }
	
	/**
	 * prepares the gameinfo to be returned in the comwaitinfo-object
	 * and sends back the object to the client
	 * 
	 * @param socket the socket to send back the object to the client
	 */
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
	
	/**
	 * This method adds a player to the game thread-safe.
	 * If the game is full at this moment, it will not be added.
	 * 
	 * @param playerId the player id
	 * @param name the name of the player
	 * @return returns true if the player was added successfully otherwise false
	 */
	public synchronized boolean addPlayer(int playerId, String name) {
		if (playerList.size() < maxPlayers)
		{
			playerList.put(playerId, name);
			return true;
		}
		return false;
	}
	
	/**
	 * This method removes a player from the game thread-safe.
	 * If the player was the creator of the game, the game will be terminated.
	 * @param playerId the id of the player
	 * @return true if game was terminated, otherwise false
	 */
	public synchronized Boolean removePlayer(int playerId) {
		if (playerId == creatorId) {	// creator left game
			gameTerminate = true;
			return true;
		}
		
		if (playerList.containsKey(playerId))
			playerList.remove(playerId);
		
		return false;
	}
	
	/**
	 * This method starts the game calculation for this game.
	 * The game calculation will be provided in an implementation of the GameCalculator.
	 */
	public void startGame()
	{
		// start the calculator thread for the gamePlay
		gamePlay = new GameDataCalculatorImpl(playerInGame);
	    
		gameStarted = true;

		generatePlayerInGame();
	}
	
	/**
	 * Sends the updated paddleposition to the gameplay
	 */
	public void updatePaddle(int userId, int paddlePosition, int paddleWidth)
	{
		gamePlay.updatePaddle(userId, paddlePosition, paddleWidth);
	}
	
	public ComGameData getComGameData()
	{
		return gamePlay.getGameData();
	}
	
	/**
	 * Generates the PlayerList for the clients to beginn with the game
	 */
	private void generatePlayerInGame()
	{
		String playerName = "";
		int count = 0;
		for (Integer playerId : playerList.keySet())
		{
			playerName = playerList.get(playerId);
			
			Player player = new Player();
			player.setUserId(playerId);
			player.setLifes(RePongDefines.DEFAULT_PLAYER_HEARTS);
			player.setPosition(0); // default position
			player.setName(playerName);
			switch(count)
			{
			case 0: 
				player.setColor(Color.RED.getRGB());
				player.setOrientation(PaddleOrientation.SOUTH);
				break;
			case 1:
				player.setColor(Color.BLUE.getRGB());
				player.setOrientation(PaddleOrientation.NORTH);
				break;
			case 2:
				player.setColor(Color.YELLOW.getRGB());
				player.setOrientation(PaddleOrientation.WEST);
				break;
			case 3:
				player.setColor(Color.GREEN.getRGB());
				player.setOrientation(PaddleOrientation.EAST);
				break;
			}
			
			playerInGame.add(player);	
			count++;
		}
	}
	
	public void terminateGame()
	{
		gameTerminate = true;
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

	public ArrayList<Player> getPlayerInGame() {
		return playerInGame;
	}
}
