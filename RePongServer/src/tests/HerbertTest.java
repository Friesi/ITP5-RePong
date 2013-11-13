package tests;
import static org.junit.Assert.*;

import org.junit.Test;

import at.frikiteysch.repong.Herbert;
import at.frikiteysch.repong.herbert.HerbertHandler;
import at.frikiteysch.repong.players.PlayerInfo;
import at.frikiteysch.repong.players.PlayerList;


public class HerbertTest {

	@Test
	public void testUpdateTimestamp() {
		System.out.println("testing update timestamp of herbert");
		
		//arrange
		int playerId = -3; // should be unique
		PlayerInfo pInfo = new PlayerInfo();
		long previousTimestamp = System.currentTimeMillis() - 100;
		pInfo.setTimeStamp(previousTimestamp);
		PlayerList.getInstance().getPlayerList().put(playerId, pInfo);
		
		Herbert herbert = new Herbert();
		herbert.setPlayerId(playerId);
		
		//act
		HerbertHandler handler = new HerbertHandler(herbert);
		handler.updateTimestamp();
		
		//assert
		boolean same = PlayerList.getInstance().getPlayerList().get(playerId).getTimeStamp() == previousTimestamp;
		assertFalse(same);
		
	}

}
