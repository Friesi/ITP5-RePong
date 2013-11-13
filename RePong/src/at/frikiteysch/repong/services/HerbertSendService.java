package at.frikiteysch.repong.services;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.logging.Logger;

import android.app.IntentService;
import android.content.Intent;
import at.frikiteysch.repong.Herbert;
import at.frikiteysch.repong.communication.CommunicationCenter;
import at.frikiteysch.repong.storage.ProfileManager;

public class HerbertSendService extends IntentService {

	private static Logger LOGGER = Logger.getLogger(HerbertSendService.class
			.getName());

	private static final int SLEEP_TIME = 4000;
	private boolean isRunning = false;
	private int playerId = -1;

	public HerbertSendService() {
		super("HerbertSendService");
		playerId = ProfileManager.getInstance().getProfile().getUserId();
		LOGGER.info("herbert service created");
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		LOGGER.info("herbert service onHandleIntent");
		isRunning = true;
		while (isRunning) {
			try {
				if (playerId < 0) // not logged in
				{
					playerId = ProfileManager.getInstance().getProfile().getUserId();
				}
				else
				{
					Herbert herbert = new Herbert();
					herbert.setPlayerId(playerId);
					
					Socket socket;
					try {
						socket = new Socket(CommunicationCenter.serverAddress, CommunicationCenter.serverPort);
						CommunicationCenter.sendComObjectToServer(socket, herbert);
						
						LOGGER.info("Herbert sent successfully");
					} catch (UnknownHostException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				
				Thread.sleep(SLEEP_TIME);
				
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	@Override
	public void onDestroy() {
		isRunning = false; // make sure service will stop
		LOGGER.info("herbert service destroyed");
	}
	
}
