package at.frikiteysch.repong;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.logging.Logger;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Toast;
import at.frikiteysch.repong.communication.CommunicationCenter;
import at.frikiteysch.repong.communication.LoginAsyncTask;
import at.frikiteysch.repong.communication.LoginAsyncTask.LoginStateReceiver;
import at.frikiteysch.repong.communication.TerminateAsync;
import at.frikiteysch.repong.helper.ValidateHelper;
import at.frikiteysch.repong.services.HerbertSendService;
import at.frikiteysch.repong.storage.ProfileManager;
import at.frikiteysch.repong.storage.RePongProfile;

public class ActivityStartScreen extends Activity implements LoginStateReceiver{

	private String userName;
	private Intent herbertIntent;
	
	private static Logger LOGGER = Logger.getLogger(ActivityStartScreen.class.getName());
	
	private DialogInterface.OnClickListener dialogTermateListener = new DialogInterface.OnClickListener() {
		@Override
		public void onClick(DialogInterface dialog, int which) {
			switch(which)
			{
			case DialogInterface.BUTTON_POSITIVE: // ok was pressed
				
				startTerminatorTask();		
				
				stopService(herbertIntent);
				
				Intent intent = new Intent(Intent.ACTION_MAIN);
				intent.addCategory(Intent.CATEGORY_HOME);
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(intent);
				break;
			}
		}
	};
		
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_screen);
        herbertIntent = new Intent(this, HerbertSendService.class);
        ProfileManager.getInstance().loadProfileFromStorage(this);
    }
    
    protected void onResume() {
       super.onResume();
       
       if (!isServiceRunning(HerbertSendService.class.getName()))
    	   startService(herbertIntent);
       else
    	   LOGGER.info("service is already running");
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
    			if (!userName.equals("")) // if user starts the app for the first time, dont show error msg 
    				intent.putExtra("Error", true);
    			
    			startActivity(intent);
    			return;
    		}
    	}
    	else
    	{
    		if (!ValidateHelper.isValidUserName(userName))
    		{
    			Intent intent = new Intent(this, ActivityProfile.class);
    			intent.putExtra("Error", true);
    			startActivity(intent);
    			return;
    		}
    	}
    	
    	//send comLogin object to server with asynctask
    	ComLogin login = new ComLogin();
    	login.setUserName(userName);
		LoginAsyncTask task = new LoginAsyncTask(this, login);
		task.execute();
		
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
    
    public void startTerminatorTask()
    {
    	// remove Player from ServerPlayerList and save Profile
    	TerminateAsync task = new TerminateAsync(this);
		task.execute();	
    }
    
    @Override
    public void onPause()
    {
    	super.onPause();
    	
    }	
    	
    @Override
	public void onBackPressed() {
    	
    	//dialog will pop up and the user will decide to close the app or not
    	AlertDialog.Builder dialog = new AlertDialog.Builder(this);
    	dialog.setMessage(R.string.msgCloseAppDialog).setPositiveButton(R.string.yes, dialogTermateListener)
    		.setNegativeButton(R.string.no, dialogTermateListener);
    	dialog.show();
	}
    
    
    private boolean isServiceRunning(String serviceName) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceName.equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Received from the login task
     */
	@Override
	public void receivedLoggedIn(ComLogin loginObject) {
		Toast.makeText(this, "Logged in with id: " + loginObject.getUserId(), Toast.LENGTH_SHORT).show();
		ProfileManager.getInstance().getProfile().setUserId(loginObject.getUserId());
		ProfileManager.getInstance().getProfile().setName(loginObject.getUserName());
	}

	/**
	 * Received from the login task
	 */
	@Override
	public void receivedError(ComError errorObject) {
		LOGGER.severe("could not login to server");
		LOGGER.severe("ERROR-Code: " + errorObject.getErrorCode());
		LOGGER.severe("ERROR-Msg: " + errorObject.getError());
	}
}
