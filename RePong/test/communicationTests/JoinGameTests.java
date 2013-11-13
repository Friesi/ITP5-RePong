package communicationTests;


import android.test.AndroidTestCase;
import at.frikiteysch.repong.ActivityJoinGame;



public class JoinGameTests extends AndroidTestCase {
	private ActivityJoinGame activityToTest;
	
	public void setUp() throws Exception {
		activityToTest = new ActivityJoinGame();
		activityToTest.startGameListRequest();
		try {
			Thread.sleep(2000); // wait before testing
		} catch (Exception e) {
			e.printStackTrace();
		};
	}

	public void test_getGameList_responseNotNull() {
		assertNotNull(activityToTest.getGameList());
		
	}

}
