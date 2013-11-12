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
		tmpGameId = lastGameId.get();	// in temporäre Variable speichern damit der lock aufgehoben werden kann
										// und nachher noch sicher die richtige Id vorhandne ist
		lock.unlock();
		
		gameMap.put(tmpGameId, game);	// Spiel zur Spielliste hinzufügen

		gameMap.get(tmpGameId).addPlayer(createGame.getCreatorId(), "SpielerName", socket);	// TODO: woher bekomm ich den spielerName??
		
		new Thread(gameMap.get(tmpGameId)).start();
	}
	
}
