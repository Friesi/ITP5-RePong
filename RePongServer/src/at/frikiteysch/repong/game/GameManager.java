package at.frikiteysch.repong.game;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

import at.frikiteysch.repong.ComCreateGame;
import at.frikiteysch.repong.ComWaitInfo;
import at.frikiteysch.repong.players.PlayerList;

public class GameManager {
	private static GameManager instance = new GameManager();
	
	private static Logger LOGGER = Logger.getLogger(GameManager.class.getName());
	
	private Map<Integer, Game> gameMap = new ConcurrentHashMap<Integer, Game>();
	
	protected GameManager()
	{
		//LOGGER.info("GameManager instance created");
	}
	
	public static GameManager getInstance()
	{
		return instance;
	}
	
	public Map<Integer, Game> getGameList() {
		return gameMap;
	}
	
	public ComWaitInfo createGame(ComCreateGame createGame) {
		
		ComWaitInfo waitInfo = new ComWaitInfo();
		waitInfo.setCreatorId(createGame.getCreatorId());
		waitInfo.setMaxPlayerCount(createGame.getMaxPlayerCount());
		//waitInfo.setPlayerList();	// TODO: PlayerList.getInstance().getPlayerList()
		
		// TODO: gameID erzeugen
		waitInfo.setGameId(1);
		
		return waitInfo;
	}
	
}
