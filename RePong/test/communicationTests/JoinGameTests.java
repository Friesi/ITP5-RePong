package communicationTests;

import java.util.Map;



import org.junit.Before;
import org.junit.Test;

import android.test.AndroidTestCase;
import at.frikiteysch.repong.ActivityJoinGame;
import at.frikiteysch.repong.GameListInfo;


public class JoinGameTests extends AndroidTestCase {
	private ActivityJoinGame activityToTest;
	
	public void setUp() throws Exception {
		activityToTest = new ActivityJoinGame();
	}

	public void test_getGameList_responseNotNull() {
		activityToTest.startGameListRequest();
		assertNotNull(activityToTest.getGameList());
		
	}

}
