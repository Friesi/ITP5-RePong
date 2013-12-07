package at.frikiteysch.repong.game;

import java.net.Socket;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import java.util.logging.Logger;

import at.frikiteysch.repong.ComCreateGame;
import at.frikiteysch.repong.ComJoinGame;
import at.frikiteysch.repong.ComLeaveGame;
import at.frikiteysch.repong.ComWaitInfo;
import at.frikiteysch.repong.GameListInfo;
import at.frikiteysch.repong.players.PlayerInfo;
import at.frikiteysch.repong.players.PlayerList;

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
	
	public static GameManager getInstance(){
		return instance;
	}
	
	public Map<Integer, Game> getGameList() {
		return gameMap;
	}
	
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
			
			gameMap.get(tmpGameId).addPlayer(createGame.getCreatorId(), pInfo.getName(), socket);
			LOGGER.info("Add Player '" + pInfo.getName() + "' to Game with Id '" + tmpGameId + "' added to GameList");
			
			new Thread(gameMap.get(tmpGameId)).start();	// Start Game
			
			getComWaitInfo(tmpGameId, socket);
		}
		else
			System.out.println("Failed to Create Game: no player with id <" + createGame.getCreatorId() + ">");
	}
	
	public void leaveGame(ComLeaveGame leaveGame) {
		Game game = gameMap.get(leaveGame.getGameId());
		
		if (game != null) {
			if (game.removePlayer(leaveGame.getUserId()))
				gameMap.remove(leaveGame.getGameId());
			
			LOGGER.log(Level.INFO, "User " + leaveGame.getUserId() + " left Game " + leaveGame.getGameId());
		}
		else {
			LOGGER.log(Level.INFO, "No Game with id " + leaveGame.getUserId() + " found!");
		}
	}
	
	public void joinGame(ComJoinGame joinGame, Socket socket) {
		PlayerInfo pInfo = PlayerList.getInstance().getPlayerList().get(joinGame.getUserId());
		
		if (pInfo != null) {
			gameMap.get(joinGame.getGameId()).addPlayer(joinGame.getUserId(), pInfo.getName(), socket);
			LOGGER.log(Level.INFO, "Player " + pInfo.getName() + " added to Game " + joinGame.getGameId());
			
			getComWaitInfo(joinGame.getGameId(), socket);
		}
	}
	
	public void getComWaitInfo(int gameId, Socket socket) {
		
		if (gameMap.containsKey(gameId))
			gameMap.get(gameId).getComWaitInfo(socket);
	}

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
	
}
