package at.frikiteysch.repong.communication;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;

import android.os.AsyncTask;
import at.frikiteysch.repong.ComTerminate;

/**
 * This class sends a termination-request to the server.
 * Therefore it uses the CommunicationCenter. It does not expect an answer from the server.
 *
 */
public class TerminateAsync extends AsyncTask<Void, Void, Void> {
	
	private static final Logger LOGGER = Logger.getLogger(TerminateAsync.class.getName());
	private int userId;
	
	public TerminateAsync(int userId) {
		this.userId = userId;
	}
	
	@Override
	protected Void doInBackground(Void... args) {
		ComTerminate comTerminate = new ComTerminate();
    	comTerminate.setUserId(userId);
    	Socket s;
		try {
			s = new Socket(CommunicationCenter.serverAddress, CommunicationCenter.serverPort);
			CommunicationCenter.sendComObjectToServer(s, comTerminate);
			
			LOGGER.info("sent terminate request successfully");
		} catch (UnknownHostException e) {
			LOGGER.log(Level.SEVERE, "no host found", e);
		} catch (IOException e) {
			LOGGER.log(Level.SEVERE, "io exception", e);
		}
		
		return null;
	}
}
