/**
 * 
 */
package at.frikiteysch.repong.players;

import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import at.frikiteysch.repong.defines.RePongDefines;

/**
 * This class is a Worker-Thread for terminating the Client if
 * there is was no "Herbert" received during a specific timescope.
 */
public class TerminatorThread implements Runnable{
	private static Logger LOGGER = Logger.getLogger(TerminatorThread.class.getName());
	
	@Override
	public void run() {
		LOGGER.log(Level.INFO, "Termination Thread started");
		while(true)
		{
			try {
				Thread.sleep(RePongDefines.SLEEP_DURATION_TERMINATOR);
				checkForExpiredPlayers();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
//		LOGGER.log(Level.INFO, "Termination Thread stopped");
	}

	private void checkForExpiredPlayers()
	{
		LOGGER.fine("check for expired players in PlayerList");
		
		PlayerList playerList = PlayerList.getInstance();
		Map<Integer, PlayerInfo> playerInfoList = playerList.getPlayerList();
		Set<Integer> playerKey = playerInfoList.keySet();
		PlayerInfo info = null;
		long currentTimestamp = System.currentTimeMillis();
		for (Integer playerId : playerKey)
		{
			info = playerInfoList.get(playerId);
			long playerTimestamp = info.getTimeStamp();
			
			if (currentTimestamp - playerTimestamp > RePongDefines.EXPIRE_TIMEOUT) 
			{
				LOGGER.fine("Player with id<" + playerId + "> expired");
				playerList.removePlayer(playerId);
			}
		}
	}
}
