package at.frikiteysch.repong.services;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;

import android.app.IntentService;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import at.frikiteysch.repong.ComError;
import at.frikiteysch.repong.ComGameData;
import at.frikiteysch.repong.ComWaitInfo;
import at.frikiteysch.repong.R;
import at.frikiteysch.repong.communication.CommunicationCenter;
import at.frikiteysch.repong.defines.RePongDefines;
import at.frikiteysch.repong.storage.ProfileManager;


/**
 * This class is used to receive an actual list of players, for a specific game.
 * It sends a request to server and gets back a list of players, which a currently in this game.
 * Therefore it is possible that if a player joins a game, every other player gets a notification and can display it.
 *
 */
public class WaitingRoomGetComWaitInfo extends IntentService {

	private static Logger LOGGER = Logger.getLogger(WaitingRoomGetComWaitInfo.class.getName());
	
	static final public String WAIT_INFO_RESULT = "at.frikiteysch.repong.WAITINGROOMGETCOMWAITINFO";
	static final public String WAIT_INFO_RESULT_PLAYERS = "at.frikiteysch.repong.WAITINGROOMGETCOMWAITINFO_PLAYERS";
	static final public String WAIT_INFO_RESULT_PLAYERCNT = "at.frikiteysch.repong.WAITINGROOMGETCOMWAITINFO_PLAYERCNT";
	public static final String WAIT_ERROR = "WAITING_ERROR";
	public static final String GAME_STARTED = "GAME_STARTED";
	
	private boolean isRunning = false;

	public WaitingRoomGetComWaitInfo() {
		super("WaitingRoomGetComWaitInfo");
		LOGGER.info("WaitingRoomGetComWaitInfo service created");
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		LOGGER.info("WaitingRoomGetComWaitInfo service onHandleIntent");
		int playerId = ProfileManager.getInstance().getProfile().getUserId();
		int gameId = intent.getExtras().getInt(getString(R.string.ComWaitInfoIntentGameId));
		isRunning = true;
		
		while (isRunning) {
			playerId = ProfileManager.getInstance().getProfile().getUserId();
			try {
				if (playerId > 0) // only if user is logged in
				{
					ComWaitInfo waitInfo = new ComWaitInfo();
					waitInfo.setGameId(gameId);
					Socket socket;
					
					try {
						socket = new Socket(CommunicationCenter.serverAddress, CommunicationCenter.serverPort);
						CommunicationCenter.sendComObjectToServer(socket, waitInfo);
						
						Object obj = CommunicationCenter.recieveComObjectFromServer(socket);
						
						if (obj instanceof ComWaitInfo) {
							LOGGER.info("GetComWaitInfo received successfully");
							
							ComWaitInfo receivedInfo = (ComWaitInfo) obj;
							String[] players = new String[] {"", "", "", ""};
						    int cnt = 0;
						    
						    for (Entry<Integer, String> entry : receivedInfo.getPlayerList().entrySet()) {
								players[cnt++] = entry.getValue();
							}
							
						    sendResult(players, receivedInfo.getMaxPlayerCount());
						}
						else if (obj instanceof ComError)
						{
							LOGGER.info("Received error in waiting room, maybe creator left the game");
							isRunning = false;
							sendError((ComError) obj);
						}
						else if (obj instanceof ComGameData)
						{
							LOGGER.info("Received GameData the first time, so game has been started by its creator");
							isRunning = false;
							sendGameStarted((ComGameData) obj);
						}
					} catch (UnknownHostException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				
				Thread.sleep(RePongDefines.SLEEP_DURATION_GETCOMWAITINFO);
				
			} catch (InterruptedException e) {
				LOGGER.log(Level.SEVERE, "interupted", e);
			}
		}
	}
	
	public void sendResult(String[] players, int maxPlayerCnt) {
	    Intent intent = new Intent(WAIT_INFO_RESULT);
	    intent.putExtra(WAIT_INFO_RESULT_PLAYERS, players);
	    intent.putExtra(WAIT_INFO_RESULT_PLAYERCNT, maxPlayerCnt);
	    
	    LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
	}
	
	private void sendError(ComError errorObj)
	{
		Intent intent = new Intent(WAIT_ERROR);
		LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
	}
	
	private void sendGameStarted(ComGameData gameData)
	{
		Intent intent = new Intent(GAME_STARTED);
		LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
	}
	
	@Override
	public void onDestroy() {
		isRunning = false; // make sure service will stop
		LOGGER.info("WaitingRoomGetComWaitInfo service destroyed");
	}
	
}
