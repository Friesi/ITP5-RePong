package at.frikiteysch.repong.communication;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import android.os.AsyncTask;
import android.widget.Toast;
import at.frikiteysch.repong.ActivityStartScreen;
import at.frikiteysch.repong.ComError;
import at.frikiteysch.repong.ComLogin;
import at.frikiteysch.repong.storage.ProfileManager;

public class LoginAsyncTask extends AsyncTask<Void, Void, Object> {
	
	private ComLogin loginObject;
	private LoginStateReceiver loginReceiver;
	
	public interface LoginStateReceiver
	{
		public void receivedLoggedIn(ComLogin loginObject);
		public void receivedError(ComError errorObject);
	}
	
	public LoginAsyncTask(LoginStateReceiver receiver, ComLogin login)
	{
		this.loginObject = login;
		this.loginReceiver = receiver;
	}

	@Override
	protected Object doInBackground(Void... args) {
		Object obj = null;
		try {
	        loginObject.setUserName(loginObject.getUserName());
	        Socket s = new Socket(CommunicationCenter.serverAddress, CommunicationCenter.serverPort);
	        s.setSoTimeout(2000);
	        CommunicationCenter.sendComObjectToServer(s, loginObject);
	        
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

	@Override
	protected void onPostExecute(Object result) {
		if (loginReceiver == null) // no receiver defined
			return;
		
		if (result == null || result instanceof ComError)
		{
			ComError errorObject = new ComError();
			errorObject.setErrorCode(-1);
			errorObject.setError("No Answer received during login");
			if (result != null)
				errorObject = (ComError) result;
			
			loginReceiver.receivedError(errorObject);
		}
		else if (result instanceof ComLogin) // success
			loginReceiver.receivedLoggedIn((ComLogin) result);
	}
}
