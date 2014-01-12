package at.frikiteysch.repong.services;

import java.util.logging.Level;
import java.util.logging.Logger;

import android.app.IntentService;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
/**
 * 
 *
 *
 */
public class GamePlayService extends IntentService{
	
	private static final Logger LOGGER = Logger.getLogger(GamePlayService.class.getName());
	
	public static final String updateRequestAction = "UPDATE_POSITION_ACTION";
	
	private boolean isRunning, readyForNextRequest;
	private Intent updateRequestIntent;
	private BroadcastReceiver receiver = new BroadcastReceiver(){
		@Override
		public void onReceive(Context ctx, Intent intent) {
			if (intent.getAction().equals("readyfornextrequest"))
				readyForNextRequest = true;
		}
	};

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
		readyForNextRequest = true;
		// register receiver
		IntentFilter filter = new IntentFilter();
	    filter.addAction("readyfornextrequest");
	    LocalBroadcastManager.getInstance(this).registerReceiver(receiver, filter);
	    
		updateRequestIntent = new Intent(updateRequestAction);
		while (isRunning) {
			try{
				Thread.sleep(20);
				if (readyForNextRequest)
				{
					readyForNextRequest = false;
					LocalBroadcastManager.getInstance(this).sendBroadcast(updateRequestIntent);
					
				}
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
