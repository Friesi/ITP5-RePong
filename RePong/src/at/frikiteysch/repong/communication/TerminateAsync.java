package at.frikiteysch.repong.communication;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import android.content.Context;
import android.os.AsyncTask;
import at.frikiteysch.repong.ComTerminate;
import at.frikiteysch.repong.storage.ProfileManager;

public class TerminateAsync extends AsyncTask<Void, Void, Void> {
	Context ctx;
	
	public TerminateAsync(Context context) {
		ctx = context;
	}
	
	@Override
	protected Void doInBackground(Void... args) {
		ComTerminate comTerminate = new ComTerminate();
    	comTerminate.setUserId(ProfileManager.getInstance().getProfile().getUserId());
    	Socket s;
		try {
			s = new Socket(CommunicationCenter.serverAddress, CommunicationCenter.serverPort);
			CommunicationCenter.sendComObjectToServer(s, comTerminate);
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// store profile
    	ProfileManager.getInstance().storeProfile(ctx);
		
		return null;
	}
}
