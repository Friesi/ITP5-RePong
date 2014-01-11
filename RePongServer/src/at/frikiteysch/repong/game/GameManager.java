package at.frikiteysch.repong.game;

import java.net.Socket;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import java.util.logging.Logger;

import at.frikiteysch.repong.ComCreateGame;
import at.frikiteysch.repong.ComError;
import at.frikiteysch.repong.ComGameData;
import at.frikiteysch.repong.ComJoinGame;
import at.frikiteysch.repong.ComLeaveGame;
import at.frikiteysch.repong.GameListInfo;
import at.frikiteysch.repong.communication.CommunicationCenter;
import at.frikiteysch.repong.defines.RePongDefines;
import at.frikiteysch.repong.players.PlayerInfo;
import at.frikiteysch.repong.players.PlayerList;

/**
 * This class is a singleton and handles all game-threads.
 * It provides methods such as create - or joingame.
 */
public class GameManager {
	private static GameManager instance;
	private static Logger LOGGER;
	
	private static AtomicInteger lastGameId = new AtomicInteger();
	private Map<Integer, Game> gameMap = new ConcurrentHashMap<Integer, Game>();
	private final Lock lock = new ReentrantLock();
	
	static {
	    LOGGER = Logger.getLogger(GameManager.class.getName());
	    instance = new GameManager();
	}
	
	protected GameManager(){
		LOGGER.info("GameManager instance created");	
	}
	
	/**
	 * @return the only instance of GameManager
	 */
	public static GameManager getInstance(){
		return instance;
	}
	
	/**
	 * @return the list of all games currently active
	 */
	public Map<Integer, Game> getGameList() {
		return gameMap;
	}
	
	/**
	 * This method creates a game with the given params.
	 * Therefore a thread with the game will be started.
	 * The ComWaitInfo will be sent back to the client if everything worked fine, otherwise
	 * the client will receive an error.
	 * @param createGame
	 * @param socket
	 */
	public void createGame(ComCreateGame createGame, Socket socket) {
		
		int tmpGameId;

		lock.lock(); // adding id thread safe
		
		Game game = new Game(lastGameId.incrementAndGet(), createGame.getMaxPlayerCount(), createGame.getGameName(), createGame.getCreatorId());
		tmpGameId = lastGameId.get();	// save to temp var so the lock can be unlocked
										// and the variable is still accessible afterwards
		lock.unlock();
		
		PlayerInfo pInfo = PlayerList.getInstance().getPlayerList().get(createGame.getCreatorId());
		if (pInfo != null) {
			gameMap.put(tmpGameId, game);	// Add Game to Gamelist
			LOGGER.info("Game with Id " + tmpGameId + " added to GameList");
			
			gameMap.get(tmpGameId).addPlayer(createGame.getCreatorId(), pInfo.getName());
			LOGGER.info("Add Player '" + pInfo.getName() + "' to Game with Id '" + tmpGameId + "' added to GameList");
			
			new Thread(gameMap.get(tmpGameId)).start();	// Start Game
			
			if (gameMap.get(tmpGameId).getMaxPlayers() != 1) {	// Normal Mode
				getComWaitInfo(tmpGameId, socket);
			}
			else { // Practice Mode
				gameMap.get(tmpGameId).startGame();
				gameMap.get(tmpGameId).getComWaitInfo(socket);
			}
		}
		else
		{
			LOGGER.info("Failed to Create Game: no player with id <" + createGame.getCreatorId() + ">");
			ComError error = new ComError(RePongDefines.Error.GENERAL_ERROR);
			CommunicationCenter.sendComObjectToClient(socket, error);
		}
	}
	
	/**
	 * This method looks up for the right game and tries to remove the 
	 * requested player from the game.
	 */
	public void leaveGame(ComLeaveGame leaveGame) {
		Game game = gameMap.get(leaveGame.getGameId());
		
		if (game != null) {
			if (game.removePlayer(leaveGame.getUserId()))
			{
				gameMap.remove(leaveGame.getGameId());
				LOGGER.log(Level.INFO, "Creator left the game, so game with id<" + leaveGame.getGameId() + "> is gone");
			}
			
			LOGGER.log(Level.INFO, "User " + leaveGame.getUserId() + " left Game " + leaveGame.getGameId());
		}
		else {
			LOGGER.log(Level.INFO, "No Game with id " + leaveGame.getUserId() + " found!");
		}
	}
	
	/**
	 * This method looks up for the game and tries to join the player to the game.
	 * If the player was successfully joined a ComWaitInfo will be returned to the client.
	 * Otherwise an error will be sent back.
	 * @param joinGame
	 * @param socket
	 */
	public void joinGame(ComJoinGame joinGame, Socket socket) {
		PlayerInfo pInfo = PlayerList.getInstance().getPlayerList().get(joinGame.getUserId());
		
		if (pInfo != null) {
			Game game = gameMap.get(joinGame.getGameId());
			if (game != null)
			{
				boolean added = game.addPlayer(joinGame.getUserId(), pInfo.getName());
				
				if (added)
				{
					LOGGER.log(Level.INFO, "Player " + pInfo.getName() + " added to Game " + joinGame.getGameId());
					getComWaitInfo(joinGame.getGameId(), socket);
				}
				else
				{
					ComError error = new ComError(RePongDefines.Error.GENERAL_ERROR);
					CommunicationCenter.sendComObjectToClient(socket, error);
					LOGGER.info("The game is full, so player could not be added to the game");
				}
			}
			else
			{
				ComError error = new ComError(RePongDefines.Error.NO_SUCH_GAME);
				CommunicationCenter.sendComObjectToClient(socket, error);
				LOGGER.info("There is no game with id<" + joinGame.getGameId() + ", so error has been sent");
			}
			
		}
	}
	
	/**
	 * This method Sends the Information about the waiting room back to the client via Socket.
	 * The game info is just about the given gameId
	 * 
	 * If there is no such game (creator left game, or other problems) an Error will be returned
	 * to the client
	 */
	public void getComWaitInfo(int gameId, Socket socket) {
		
		if (gameMap.containsKey(gameId))
		{
			Game game = gameMap.get(gameId);
			if (!game.getGameStarted())
				game.getComWaitInfo(socket);
			else
			{
				ComGameData gameData = new ComGameData();
				gameData.setBall(null);
				gameData.setField(null);
				gameData.setPlayerList(game.getPlayerInGame()); // game is started so playerIngame is available
				
				CommunicationCenter.sendComObjectToClient(socket, gameData);
			}
		}
		else
		{
			ComError error = new ComError(RePongDefines.Error.NO_SUCH_GAME);
			CommunicationCenter.sendComObjectToClient(socket, error);
			LOGGER.info("There is no game with id<" + gameId + ", so error has been sent");
		}
	}

	/**
	 * prepares all available games in a gamelist
	 * @return a map with the gamelistinfo as value and the gameId as key
	 */
	public Map<Integer, GameListInfo> getGameListInfo() {
		Map<Integer,GameListInfo> returnMap = new ConcurrentHashMap<Integer,GameListInfo>();
		int i=0;
		for(Game game:this.getGameList().values()){
			GameListInfo info = new GameListInfo();
			info.setGameName(game.getGameName());
			info.setMaxPlayerCount(game.getMaxPlayers());
			info.setGameId(game.getGameId());
			info.setCurPlayerCount(game.getPlayerList().size());
			info.setCreatorName(game.getCreatorName());
			returnMap.put(i,info);
			i++;
		}
		return returnMap;
	}
	
	/**
	 * This method sends the new paddle position to the according game
	 * and sends back the current gamedata to the client
	 */
	public void handlePaddlePosition(int gameId, int userId, int paddlePosition, int paddleWidth, Socket socket)
	{
		if (gameMap.containsKey(gameId))
		{
			Game game = gameMap.get(gameId);
			game.updatePaddle(userId, paddlePosition, paddleWidth);
			ComGameData gameData = game.getComGameData();
			
			CommunicationCenter.sendComObjectToClient(socket, gameData);
		}
		else
		{
			ComError error = new ComError(RePongDefines.Error.NO_SUCH_GAME);
			CommunicationCenter.sendComObjectToClient(socket, error);
			LOGGER.info("There is no game with id<" + gameId + ">, so error has been sent: userId = " + userId);
		}
	}
	
	/**
	 * This method gets the proper game from the game-list
	 * and send a start-request to it.
	 * Afterwards it sends back the gamedata to the client.
	 * @param gameId the game to start
	 */
	public void startGame(int gameId, Socket socket)
	{
		Game game = gameMap.get(gameId);
		
		if (game != null)
		{
			game.startGame();
			
			ComGameData gameData = new ComGameData();
			gameData.setBall(null);
			gameData.setField(null);
			gameData.setPlayerList(game.getPlayerInGame());
			CommunicationCenter.sendComObjectToClient(socket, gameData);
		}
		else
		{
			LOGGER.severe("No game with id<" + gameId + "> found!");
			ComError error = new ComError(RePongDefines.Error.NO_SUCH_GAME);
			CommunicationCenter.sendComObjectToClient(socket, error);
			
		}
	}
}
