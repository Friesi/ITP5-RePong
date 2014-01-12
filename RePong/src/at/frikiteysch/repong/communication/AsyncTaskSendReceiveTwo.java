package at.frikiteysch.repong.communication;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;

import android.os.AsyncTask;
import at.frikiteysch.repong.ComError;
import at.frikiteysch.repong.defines.RePongDefines;

/**
 * This class is the same as AsyncTaskSendReceive. 
 * It's needed for ActivityJoinGame, to send more than one type of object.
 *
 * @param <Tsend> the object which will be sent
 * @param <Tresult> the object which will be received
 */

public class AsyncTaskSendReceiveTwo<Tsend, Tresult> extends AsyncTask<Void, Void, Object> {	// same as AsyncTaskSendReceive -- needed for ActivityJoinGame
	
	private Class<Tresult> resultType;
	private Tsend sendObject;
	private AsyncTaskStateReceiverTwo<Tresult> asyncTaskReceiver;
	private int SOCKET_CONNECTION_TIMEOUT = 0;//5000;
	
	private Logger LOGGER = Logger.getLogger(AsyncTaskSendReceiveTwo.class.getName());
	
	public interface AsyncTaskStateReceiverTwo<I>
	{
		public void receivedOkResultTwo(I resultObject);
		public void receivedErrorTwo(ComError errorObject);
	}
	
	public AsyncTaskSendReceiveTwo(Class<Tresult> resultType, AsyncTaskStateReceiverTwo<Tresult> receiver, Tsend sendObject)
	{
		this.resultType = resultType;
		this.sendObject = sendObject;
		this.asyncTaskReceiver = receiver;
	}

	@Override
	protected Object doInBackground(Void... args) {
		Object obj = null;
		try {
	        //loginObject.setUserName(loginObject.getUserName());
			Socket s = new Socket();
	        s.connect(new InetSocketAddress(CommunicationCenter.serverAddress, CommunicationCenter.serverPort), SOCKET_CONNECTION_TIMEOUT);
	        CommunicationCenter.sendComObjectToServer(s, sendObject);
	        
	        // Answer from server
	        obj = CommunicationCenter.recieveComObjectFromServer(s);
		} catch (SocketTimeoutException ste) {
			LOGGER.log(Level.SEVERE, "got socket timeout during connection", ste);
		} catch (UnknownHostException e) {
			LOGGER.log(Level.SEVERE, "unknown host exception in async task", e);
        } catch (IOException e) {
        	LOGGER.log(Level.SEVERE, "error IO exception in async task", e);
		}
		
		return obj;
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void onPostExecute(Object result) {
		if (asyncTaskReceiver == null) // no receiver defined
			return;
		
		if (result == null || result instanceof ComError)
		{
			ComError errorObject = new ComError(RePongDefines.Error.NO_ANSWER_ASYNCTASK);
			if (result != null)
				errorObject = (ComError) result;
			
			asyncTaskReceiver.receivedErrorTwo(errorObject);
		}
		else if (this.resultType.isInstance(result)) // success
			asyncTaskReceiver.receivedOkResultTwo((Tresult) result);
	}
}
