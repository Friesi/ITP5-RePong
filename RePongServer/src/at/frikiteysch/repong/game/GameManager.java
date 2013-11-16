package at.frikiteysch.repong.game;

import java.net.Socket;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Logger;

import at.frikiteysch.repong.ComCreateGame;
import at.frikiteysch.repong.ComWaitInfo;
import at.frikiteysch.repong.GameListInfo;
import at.frikiteysch.repong.players.PlayerInfo;
import at.frikiteysch.repong.players.PlayerList;

public class GameManager {
	private static GameManager instance = new GameManager();
	private static Logger LOGGER = Logger.getLogger(GameManager.class.getName());
	
	private static AtomicInteger lastGameId = new AtomicInteger();
	private Map<Integer, Game> gameMap = new ConcurrentHashMap<Integer, Game>();
	private final Lock lock = new ReentrantLock();
	
	protected GameManager()
	{
		//LOGGER.info("GameManager instance created");	// TODO: erzeugt eine NullPointer Exception warum auch immer ^^
	}
	
	public static GameManager getInstance()
	{
		return instance;
	}
	
	public Map<Integer, Game> getGameList() {
		return gameMap;
	}
	
	public void createGame(ComCreateGame createGame, Socket socket) {
		
		int tmpGameId;

		lock.lock(); // adding id thread safe
		
		Game game = new Game(lastGameId.incrementAndGet(), createGame.getMaxPlayerCount(), createGame.getGameName(), createGame.getCreatorId());
		tmpGameId = lastGameId.get();	// in tempor�re Variable speichern damit der lock aufgehoben werden kann
										// und nachher noch sicher die richtige Id vorhandne ist
		lock.unlock();
		
		gameMap.put(tmpGameId, game);	// Spiel zur Spielliste hinzuf�gen

		gameMap.get(tmpGameId).addPlayer(createGame.getCreatorId(), "SpielerName", socket);	// TODO: woher bekomm ich den spielerName??
		
		new Thread(gameMap.get(tmpGameId)).start();
	}

	public Map<Integer, GameListInfo> getGameListInfo() {
		Map<Integer,GameListInfo> returnMap = new ConcurrentHashMap<Integer,GameListInfo>();
		//TODO fill map with info
		int i=0;
		for(Game game:this.getGameList().values()){
			GameListInfo info = new GameListInfo();
			info.setGameName(game.getGameName());
			info.setMaxPlayerCount(game.getMaxPlayers());
			info.setGameId(game.getGameId());
			info.setCurPlayerCount(game.getPlayerList().size());
			info.setCreatorName("blub"); //TODO obtain creators name
			returnMap.put(i,info);
			i++;
		}
		return returnMap;
	}
	
}
