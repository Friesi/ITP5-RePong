package at.frikiteysch.repong.services;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.logging.Logger;

import android.app.IntentService;
import android.content.Intent;
import at.frikiteysch.repong.Herbert;
import at.frikiteysch.repong.communication.CommunicationCenter;
import at.frikiteysch.repong.defines.RePongDefines;
import at.frikiteysch.repong.storage.ProfileManager;

public class HerbertSendService extends IntentService {

	private static Logger LOGGER = Logger.getLogger(HerbertSendService.class
			.getName());

	private boolean isRunning = false;

	public HerbertSendService() {
		super("HerbertSendService");
		LOGGER.info("herbert service created");
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		LOGGER.info("herbert service onHandleIntent");
		int playerId = ProfileManager.getInstance().getProfile().getUserId();
		isRunning = true;
		while (isRunning) {
			playerId = ProfileManager.getInstance().getProfile().getUserId();
			try {
				if (playerId > 0) // only if user is logged in
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
				
				Thread.sleep(RePongDefines.SLEEP_DURATION_HERBERT);
				
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
