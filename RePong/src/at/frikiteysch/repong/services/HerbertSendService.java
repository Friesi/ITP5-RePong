package at.frikiteysch.repong.services;

import java.util.logging.Logger;

import android.app.IntentService;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

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
		isRunning = true;
		while (isRunning) {
			try {
				Thread.sleep(2000);
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
