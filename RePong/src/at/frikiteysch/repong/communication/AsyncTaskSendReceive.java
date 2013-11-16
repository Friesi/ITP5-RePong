package at.frikiteysch.repong.communication;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import android.os.AsyncTask;
import at.frikiteysch.repong.ComError;

public class AsyncTaskSendReceive<Tsend, Tresult> extends AsyncTask<Void, Void, Object> {
	
	private Class<Tresult> resultType;
	private Tsend sendObject;
	private ParcelableSocket socket;
	private AsyncTaskStateReceiver<Tresult> asyncTaskReceiver;
	
	public interface AsyncTaskStateReceiver<I>
	{
		public void receivedOkResult(I resultObject, ParcelableSocket socket);
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
			socket = new ParcelableSocket(CommunicationCenter.serverAddress, CommunicationCenter.serverPort);	// This Parcelable Socket is needed because otherwise we cannot send it to another activity
			socket.setSoTimeout(2000);
	        CommunicationCenter.sendComObjectToServer(socket, sendObject);
	        
	        // Answer from server
	        obj = CommunicationCenter.recieveComObjectFromServer(socket);
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();  
        } catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
			ComError errorObject = new ComError();
			errorObject.setErrorCode(-1);
			errorObject.setError("No Answer received");
			if (result != null)
				errorObject = (ComError) result;
			
			asyncTaskReceiver.receivedError(errorObject);
		}
		else if (this.resultType.isInstance(result)) // success
			asyncTaskReceiver.receivedOkResult((Tresult) result, this.socket);
	}
}
