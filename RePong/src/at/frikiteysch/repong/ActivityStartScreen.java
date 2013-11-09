package at.frikiteysch.repong;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Toast;
import at.frikiteysch.repong.communication.CommunicationCenter;
import at.frikiteysch.repong.helper.ValidateHelper;
import at.frikiteysch.repong.storage.ProfileManager;
import at.frikiteysch.repong.storage.RePongProfile;

public class ActivityStartScreen extends Activity {

	private String userName;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_screen);
        ProfileManager.getInstance().loadProfileFromStorage(this);
    }
    
    protected void onResume() {
       super.onResume();
       
       RePongProfile profile = ProfileManager.getInstance().getProfile();
       if (profile.getUserId() < 0) // not logged in
       {
    	   logginUserOrReturnToStartScreen();
       }
    }
    
    private void logginUserOrReturnToStartScreen()
    {
    	String profileName = ProfileManager.getInstance().getProfile().getName();
    	userName = profileName;
    	if (profileName.equals("")) // no username entered before
    	{
    		String userNameFromStartScreen = getIntent().getStringExtra("userName");
    		if (ValidateHelper.isValidUserName(userNameFromStartScreen))
    		{
    			userName = userNameFromStartScreen;
    		}
    		else // no valid user name, so set start screen again
    		{
    			Intent intent = new Intent(this, ActivityFirstStartScreen.class);
    			intent.putExtra("Error", true);
    			startActivity(intent);
    			return;
    		}
    	}
    	else
    	{
    		//TODO check if userName is valid
    	}
    	
    	//send comLogin object to server with asynctask
		SendLoginAsync task = new SendLoginAsync();
		task.execute();
		
    }
    
    private class SendLoginAsync extends AsyncTask<Void, Void, ComLogin> {

		@Override
		protected ComLogin doInBackground(Void... args) {

			ComLogin comLoginObject = null;
			
			try {
		        ComLogin loginObject = new ComLogin();
		        loginObject.setUserName(userName);
		        Socket s = new Socket(CommunicationCenter.serverAddress, CommunicationCenter.serverPort);
		        CommunicationCenter.sendComObjectToServer(s, loginObject);		        
		        
		        // Answer from server
		        comLoginObject = (ComLogin) CommunicationCenter.recieveComObjectFromServer(s);
		        
	        } catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			return comLoginObject;
		}

		@Override
		protected void onPostExecute(ComLogin result) {
			if (result != null)
			{
				Toast.makeText(ActivityStartScreen.this, "PlayerId: " + result.getUserId(), Toast.LENGTH_LONG).show();
				ProfileManager.getInstance().getProfile().setName(result.getUserName());
				ProfileManager.getInstance().getProfile().setUserId(result.getUserId());
			}
		}
	}
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
    public void btnCreateGameOnClick(View v) {
    	Intent myIntent = new Intent(this, ActivityCreateGame.class);
        this.startActivity(myIntent);
    }
    
    public void btnJoinGameOnClick(View v) {
    	Intent myIntent = new Intent(this, ActivityJoinGame.class);
        this.startActivity(myIntent);
    }
    
    public void btnProfileOnClick(View v) {
    	Intent myIntent = new Intent(this, ActivityProfile.class);
        this.startActivity(myIntent);
    }
    
    // Nur als Test : TODO: auslagern
    public void btnGameStartOnClick(View v) {
    	Intent myIntent = new Intent(this, MovePaddle.class);
        //myIntent.putExtra("key", value); //Optional parameters
        this.startActivity(myIntent);
    }
    
    @Override
    public void onPause()
    {
    	super.onPause();
    	// store profile
    	ProfileManager.getInstance().storeProfile(this);
    }
    
    @Override
	public void onBackPressed() {
		Intent intent = new Intent(Intent.ACTION_MAIN);
		intent.addCategory(Intent.CATEGORY_HOME);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(intent);
	}
}
