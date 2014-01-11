package at.frikiteysch.repong.herbert;
import at.frikiteysch.repong.Herbert;
import at.frikiteysch.repong.players.PlayerInfo;
import at.frikiteysch.repong.players.PlayerList;

/**
 * This class can handle Herbert-Objects and updates the timestamp of 
 * the given user appropriately
 */
public class HerbertHandler {
	
	private Herbert herbert;
	
	public HerbertHandler(Herbert herbert)
	{
		this.herbert = herbert;
	}
	
	/**
	 * this method takes the playerid of the herbert-object
	 * and updates the timestamp of the object in the playerlist to the current time
	 */
	public void updateTimestamp()
	{
		PlayerInfo pInfo = PlayerList.getInstance().getPlayerList().get(herbert.getPlayerId());
		if (pInfo != null)
			pInfo.setTimeStamp(System.currentTimeMillis());
		else
			System.out.println("no player with id <" + herbert.getPlayerId() + ">");
	}
}
