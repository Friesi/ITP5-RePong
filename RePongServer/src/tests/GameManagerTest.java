package tests;

import static org.junit.Assert.*;

import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.junit.Before;
import org.junit.Test;

import at.frikiteysch.repong.GameListInfo;
import at.frikiteysch.repong.ServerMain;
import at.frikiteysch.repong.game.Game;
import at.frikiteysch.repong.game.GameManager;

public class GameManagerTest {
	private static Logger LOGGER = Logger.getLogger(GameManagerTest.class.getName());
	private GameManager testManager;
	@Before
	public void setUp() throws Exception {
		testManager = GameManager.getInstance();
		for(int i=1;i<=4;i++){
			Game game = new Game(i,4,"supergame"+i,2);
			testManager.getGameList().put(i, game);
		}
	}

	@Test
	public void test_getGameListInfo_sizeOfMap() {
		//arrange
		//act
		Map<Integer,GameListInfo> result = testManager.getGameListInfo();
		//assert
		assertEquals(4, result.size());
	}
	
	@Test
	public void test_getGameListInfo_contentCheckNotNull() {
		//arrange
		//act
		Map<Integer,GameListInfo> result = testManager.getGameListInfo();
		//assert
		for (int i=0; i<4;i++){
		GameListInfo toTestInfo = result.get(i);
		assertNotNull(toTestInfo);
		
		assertNotNull(toTestInfo.getCreatorName());
		LOGGER.log(Level.INFO, "CreatorName: "+toTestInfo.getCreatorName());
		
		assertNotNull(toTestInfo.getCurPlayerCount());
		LOGGER.log(Level.INFO, "CurPlayerCount: "+toTestInfo.getCurPlayerCount());
		
		assertNotNull(toTestInfo.getGameId());
		LOGGER.log(Level.INFO, "GameId: "+toTestInfo.getGameId());
		
		assertNotNull(toTestInfo.getGameName());
		LOGGER.log(Level.INFO, "GameName: "+toTestInfo.getGameName());
		
		assertNotNull(toTestInfo.getMaxPlayerCount());
		LOGGER.log(Level.INFO, "MaxPlayerCount: "+toTestInfo.getMaxPlayerCount());
		LOGGER.log(Level.OFF, "****************");
		}
	}

}
