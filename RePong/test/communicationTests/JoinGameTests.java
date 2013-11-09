package communicationTests;

import static org.junit.Assert.*;

import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import at.frikiteysch.repong.ActivityJoinGame;
import at.frikiteysch.repong.GameListInfo;


public class JoinGameTests {
	private ActivityJoinGame activityToTest;
	@Before
	public void setUp() throws Exception {
		activityToTest = new ActivityJoinGame();
	}

	@Test
	public void getGameList_responseNotNull_test() {
		Map<Integer, GameListInfo> result= activityToTest.getGameListRequest();
		assertNotNull(result);
		
	}

}
