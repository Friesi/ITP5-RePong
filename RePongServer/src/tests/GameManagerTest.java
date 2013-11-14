package tests;

import static org.junit.Assert.*;

import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import at.frikiteysch.repong.GameListInfo;
import at.frikiteysch.repong.game.Game;
import at.frikiteysch.repong.game.GameManager;

public class GameManagerTest {
	private GameManager testManager;
	@Before
	public void setUp() throws Exception {
		testManager = GameManager.getInstance();
		for(int i=1;i<=4;i++){
			Game game = new Game(1,4,"supergame"+i,2);
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
	

}
