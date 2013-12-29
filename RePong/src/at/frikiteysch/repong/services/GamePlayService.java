package at.frikiteysch.repong.services;

import java.util.logging.Level;
import java.util.logging.Logger;

import android.app.IntentService;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

public class GamePlayService extends IntentService{
	
	private static final Logger LOGGER = Logger.getLogger(GamePlayService.class.getName());
	
	public static final String updateRequestAction = "UPDATE_POSITION_ACTION";
	
	private boolean isRunning;
	private Intent updateRequestIntent;

	public GamePlayService(String name) {
		super(name);
	}
	
	public GamePlayService()
	{
		super("GamePlayService");
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		LOGGER.info("GamePlayService started");
		isRunning = true;
		updateRequestIntent = new Intent(updateRequestAction);
		while (isRunning) {
			try{
				LocalBroadcastManager.getInstance(this).sendBroadcast(updateRequestIntent);
				Thread.sleep(300);
			} catch (InterruptedException e)
			{
				LOGGER.log(Level.SEVERE, "Thread interrupted in gameplayservice", e);
			}
		}
	}
	
	@Override
	public void onDestroy() {
		isRunning = false; // make sure service will stop
		LOGGER.info("GamePlayService destroyed");
	}
}
