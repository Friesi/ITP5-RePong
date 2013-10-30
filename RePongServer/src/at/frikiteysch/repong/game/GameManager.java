package at.frikiteysch.repong.game;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

public class GameManager {
	private static GameManager instance = new GameManager();
	
	private static Logger LOGGER = Logger.getLogger(GameManager.class.getName());
	
	private Map<Integer, Game> gameMap = new ConcurrentHashMap<Integer, Game>();
	
	protected GameManager()
	{
		LOGGER.info("GameManager instance created");
	}
	
	public GameManager getInstance()
	{
		return instance;
	}
	
}
