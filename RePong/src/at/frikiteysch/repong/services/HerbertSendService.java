package at.frikiteysch.repong.services;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.logging.Level;
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
						socket = new Socket();
						socket.connect(new InetSocketAddress(CommunicationCenter.serverAddress, CommunicationCenter.serverPort), 5000);
						CommunicationCenter.sendComObjectToServer(socket, herbert);
						
						LOGGER.info("Herbert sent successfully");
					} catch (SocketTimeoutException ste)
					{
						LOGGER.log(Level.SEVERE, "could not connect to address due to timeout", ste);
						stopHerbert();
					} catch (UnknownHostException e) {
						LOGGER.log(Level.SEVERE, "unknow host exception in herbert service", e);
						stopHerbert();
					} catch (IOException e) {
						LOGGER.log(Level.SEVERE, "io exception in herbert service", e);
						stopHerbert();
					}
				}
				
				Thread.sleep(RePongDefines.SLEEP_DURATION_HERBERT);
				
			} catch (InterruptedException e) {
				LOGGER.log(Level.SEVERE, "unexpected interuption of herber service", e);
				stopHerbert();
			}
		}
	}

	@Override
	public void onDestroy() {
		isRunning = false; // make sure service will stop
		LOGGER.info("herbert service destroyed");
	}
	
	public void stopHerbert()
	{
		LOGGER.info("nothing can stop herbert!!!");
		LOGGER.info("will stop herbert service now");
		isRunning = false;
		
	}
	
}
