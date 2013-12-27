package at.frikiteysch.repong.communication;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;

import android.os.AsyncTask;

public class AsyncTaskSend<Tsend> extends AsyncTask<Void, Void, Void> {
	private Logger LOGGER = Logger.getLogger(AsyncTaskSend.class.getName());
	
	private static final int SOCKET_CONNECTION_TIMEOUT = 5000;
	private Tsend sendObject;
	private PostExecutionReceiver receiver;
	
	public interface PostExecutionReceiver
	{
		public void onPostExecution();
	}
	
	public AsyncTaskSend(Tsend sendObject)
	{
		this(sendObject, null);
	}
	
	public AsyncTaskSend(Tsend sendObject, PostExecutionReceiver receiver)
	{
		this.sendObject = sendObject;
		this.receiver = receiver;
	}

	@Override
	protected Void doInBackground(Void... params) {
		try {
			Socket s = new Socket();
	        s.connect(new InetSocketAddress(CommunicationCenter.serverAddress, CommunicationCenter.serverPort), SOCKET_CONNECTION_TIMEOUT);
	        CommunicationCenter.sendComObjectToServer(s, sendObject);
		} catch (SocketTimeoutException ste) {
			LOGGER.log(Level.SEVERE, "got socket timeout during connection", ste);
		} catch (UnknownHostException e) {
			LOGGER.log(Level.SEVERE, "unknown host exception in async task", e);
        } catch (IOException e) {
        	LOGGER.log(Level.SEVERE, "error IO exception in async task", e);
		}
		return null;
	}
	
	@Override
	protected void onPostExecute(Void result) {
		if (receiver != null)
			receiver.onPostExecution();
	}

}
