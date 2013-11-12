package at.frikiteysch.repong.communication;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import android.os.AsyncTask;
import at.frikiteysch.repong.ComError;

public class AsyncTaskSendReceive<Tsend, Tresult> extends AsyncTask<Void, Void, Object> {
	
	private Class<Tresult> resultType;
	private Tsend sendObject;
	private AsyncTaskStateReceiver<Tresult> asyncTaskReceiver;
	
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
	
	/*
	public static <T> AsyncTaskSendReceive<T> createMyObject(Class<T> type, AsyncTaskStateReceiver<T> receiver, T sendObject) {
	    return new AsyncTaskSendReceive<T>(type, receiver, sendObject);
	 }*/

	@Override
	protected Object doInBackground(Void... args) {
		Object obj = null;
		try {
	        //loginObject.setUserName(loginObject.getUserName());
	        Socket s = new Socket(CommunicationCenter.serverAddress, CommunicationCenter.serverPort);
	        s.setSoTimeout(2000);
	        CommunicationCenter.sendComObjectToServer(s, sendObject);
	        
	        // Answer from server
	        obj = CommunicationCenter.recieveComObjectFromServer(s);
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
			errorObject.setError("No Answer received during login");
			if (result != null)
				errorObject = (ComError) result;
			
			asyncTaskReceiver.receivedError(errorObject);
		}
		else if (this.resultType.isInstance(result)) // success
			asyncTaskReceiver.receivedOkResult((Tresult) result);
	}
}
