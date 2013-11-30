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

public class AsyncTaskSendReceive<Tsend, Tresult> extends AsyncTask<Void, Void, Object> {
	
	private Class<Tresult> resultType;
	private Tsend sendObject;
	private AsyncTaskStateReceiver<Tresult> asyncTaskReceiver;
	private int SOCKET_CONNECTION_TIMEOUT = 5000;
	
	private Logger LOGGER = Logger.getLogger(AsyncTaskSendReceive.class.getName());
	
	public interface AsyncTaskStateReceiver<I>
	{
		public void receivedOkResult(I resultObject);
		public void receivedError(ComError errorObject);
	}
	
	public AsyncTaskSendReceive(Class<Tresult> resultType, AsyncTaskStateReceiver<Tresult> receiver, Tsend sendObject)
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
			
			asyncTaskReceiver.receivedError(errorObject);
		}
		else if (this.resultType.isInstance(result)) // success
			asyncTaskReceiver.receivedOkResult((Tresult) result);
	}
}
