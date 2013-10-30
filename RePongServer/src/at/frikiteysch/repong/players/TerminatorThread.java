/**
 * 
 */
package at.frikiteysch.repong.players;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import at.frikiteysch.repong.ComReturnMessage;

/**
 * This class is a Worker-Thread for terminating the Client if
 * there is was no "Herbert" received during a specific timescope.
 */
public class TerminatorThread implements Runnable{
	private static Logger LOGGER = Logger.getLogger(TerminatorThread.class.getName());
	private static int EXPIRE_TIME_DURATION = 10000;
	private static int EXPIRE_CHECK_DISTANCE = 10000;
	
	@Override
	public void run() {
		LOGGER.log(Level.INFO, "Termination Thread started");
		while(true)
		{
			try {
				Thread.sleep(EXPIRE_CHECK_DISTANCE);
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
			
			if (currentTimestamp - playerTimestamp > EXPIRE_TIME_DURATION) 
			{
				LOGGER.fine("Player with id<" + playerId + "> expired");
				//send message to player and remove from list
				ComReturnMessage terminationMessage = new ComReturnMessage();
				terminationMessage.setMessageId(0); // TODO which id????
				try {
					ObjectOutputStream objectStream = new ObjectOutputStream(info.getS().getOutputStream());
					objectStream.writeObject(terminationMessage);
					LOGGER.fine("Sent ComReturnMessage to Client");
					
					playerList.removePlayer(playerId);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
}
