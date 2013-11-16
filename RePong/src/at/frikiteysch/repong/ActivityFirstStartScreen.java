package at.frikiteysch.repong;

import java.util.logging.Logger;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import at.frikiteysch.repong.communication.AsyncTaskSendReceive;
import at.frikiteysch.repong.communication.ParcelableSocket;
import at.frikiteysch.repong.communication.AsyncTaskSendReceive.AsyncTaskStateReceiver;
import at.frikiteysch.repong.helper.ValidateHelper;
import at.frikiteysch.repong.storage.ProfileManager;

public class ActivityFirstStartScreen extends Activity implements AsyncTaskStateReceiver<ComLogin>{

	private static Logger LOGGER = Logger.getLogger(ActivityFirstStartScreen.class.getName());
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_start_screen);
    }
	
	@Override
	protected void onResume()
	{
		super.onResume();
	}
	
	public void btnOkOnClick(View v) {
		EditText userNameTxt = (EditText)findViewById(R.id.txtName);
		String userName = userNameTxt.getText().toString();
		
		if(ValidateHelper.isValidUserName(userName)) {
			View progressBar = findViewById(R.id.loginProgressBar);
			progressBar.setVisibility(View.VISIBLE);
			
			ComLogin loginObject = new ComLogin();
			loginObject.setUserName(userName);
			
			AsyncTaskSendReceive<ComLogin, ComLogin> tranceiveTask = new AsyncTaskSendReceive<ComLogin, ComLogin>(ComLogin.class, this, loginObject);
			tranceiveTask.execute();
		}
		else
			Toast.makeText(this, R.string.invalidUserName, Toast.LENGTH_LONG).show();
    }
	
	public void btnExitOnClick(View v) {
		onBackPressed();
    }
	
	@Override
	public void onBackPressed() {
		Intent intent = new Intent(Intent.ACTION_MAIN);
		intent.addCategory(Intent.CATEGORY_HOME);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(intent);
	}

	@Override
	public void receivedOkResult(ComLogin resultObject, ParcelableSocket socket) {
		// set user as logged in in profilemanager
		ProfileManager.getInstance().getProfile().setName(resultObject.getUserName());
		ProfileManager.getInstance().getProfile().setUserId(resultObject.getUserId());
		
		// start start-screen
		Intent myIntent = new Intent(this, ActivityStartScreen.class);
		this.startActivity(myIntent);
		finish();
	}

	@Override
	public void receivedError(ComError errorObject) {
		// hide progress bar
		View progressBar = findViewById(R.id.loginProgressBar);
		progressBar.setVisibility(View.INVISIBLE);
		
		LOGGER.severe("There went something wrong during login process");
		LOGGER.severe("CODE: " + errorObject.getErrorCode());
		LOGGER.severe("MSG: " + errorObject.getError());
		Toast.makeText(this, R.string.upps, Toast.LENGTH_SHORT).show();
	}
}
