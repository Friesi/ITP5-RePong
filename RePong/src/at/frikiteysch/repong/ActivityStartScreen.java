package at.frikiteysch.repong;

import java.util.logging.Logger;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Toast;
import at.frikiteysch.repong.communication.AsyncTaskSend;
import at.frikiteysch.repong.communication.AsyncTaskSendReceive;
import at.frikiteysch.repong.communication.AsyncTaskSendReceive.AsyncTaskStateReceiver;
import at.frikiteysch.repong.communication.CommunicationCenter;
import at.frikiteysch.repong.services.HerbertSendService;
import at.frikiteysch.repong.storage.ProfileManager;
import at.frikiteysch.repong.storage.RePongProfile;
/**
 * 
 * This activity shows the main menu and handles the click events as well as 
 * the user login and closing the app.
 * 
 *
 */
public class ActivityStartScreen extends Activity implements AsyncTaskStateReceiver<ComLogin>{

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
        CommunicationCenter.loadProperties(this);
        setContentView(R.layout.activity_start_screen);
        herbertIntent = new Intent(this, HerbertSendService.class);
        ProfileManager.getInstance().loadProfileFromStorage(this);
    }
    
    protected void onResume() {
       super.onResume();
       
       RePongProfile profile = ProfileManager.getInstance().getProfile();
       
       if (profile.getName().equals("")) // first time opening the app, no username
       {
    	   Intent intent = new Intent(this, ActivityFirstStartScreen.class);
    	   startActivity(intent);
       }
       else if (profile.getUserId() < 0) // name exists, but isn't logged in
       {
    	   logginUser();
       }
       else // user is logged in
       {
    	   if (!isServiceRunning(HerbertSendService.class.getName()))
        	   startService(herbertIntent);
           else
        	   LOGGER.info("service is already running");
       }
    }
    
    private void hideLoginIndications()
    {
    	View progressBar = findViewById(R.id.loginProgressBar);
    	View progressMessage = findViewById(R.id.logginProgressMessage);
    	
    	progressBar.setVisibility(View.INVISIBLE);
    	progressMessage.setVisibility(View.INVISIBLE);
    }
    
    private void showLoginIndications()
    {
    	View progressBar = findViewById(R.id.loginProgressBar);
    	View progressMessage = findViewById(R.id.logginProgressMessage);
    	
    	progressBar.setVisibility(View.VISIBLE);
    	progressMessage.setVisibility(View.VISIBLE);
    }
    
    private void logginUser()
    {
    	showLoginIndications();
    	
    	//send comLogin object to server with asynctask
    	ComLogin login = new ComLogin();
    	login.setUserName(ProfileManager.getInstance().getProfile().getName());
		
    	AsyncTaskSendReceive<ComLogin, ComLogin> task = 
    			new AsyncTaskSendReceive<ComLogin, ComLogin>(ComLogin.class, this, login);

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
    	ComTerminate terminateObject = new ComTerminate();
    	terminateObject.setUserId(ProfileManager.getInstance().getProfile().getUserId());
    	AsyncTaskSend<ComTerminate> terminatorTask = new AsyncTaskSend<ComTerminate>(terminateObject);
    	terminatorTask.execute();
		
		// store profile
    	ProfileManager.getInstance().storeProfile(this);
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
	public void receivedOkResult(ComLogin resultObject) {
		hideLoginIndications();
		Toast.makeText(this, "Logged in with id: " + resultObject.getUserId(), Toast.LENGTH_SHORT).show();
		ProfileManager.getInstance().getProfile().setUserId(resultObject.getUserId());
		ProfileManager.getInstance().getProfile().setName(resultObject.getUserName());
		startService(herbertIntent);
	}

	/**
	 * Received from the login task
	 */
	@Override
	public void receivedError(ComError errorObject) {
		hideLoginIndications();
		Toast.makeText(this, "Error during login-progress", Toast.LENGTH_SHORT).show();
		LOGGER.severe("could not login to server in start acitivty");
		LOGGER.severe(errorObject.printError());
	}
}
