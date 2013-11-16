package at.frikiteysch.repong;

import java.util.logging.Logger;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import at.frikiteysch.repong.communication.AsyncTaskSendReceive;
import at.frikiteysch.repong.communication.ParcelableSocket;
import at.frikiteysch.repong.communication.AsyncTaskSendReceive.AsyncTaskStateReceiver;
import at.frikiteysch.repong.communication.TerminateAsync;
import at.frikiteysch.repong.helper.ValidateHelper;
import at.frikiteysch.repong.storage.ProfileManager;

public class ActivityProfile extends Activity implements AsyncTaskStateReceiver<ComLogin>{

	private static Logger LOGGER = Logger.getLogger(ActivityProfile.class.getName());
	private TextView userName;
	private Button btnChange;
	private View viewBtnCancelChange;
	
	private String previousUserName;
	
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        
        userName = (TextView)findViewById(R.id.txtUserName);
        userName.setEnabled(false);		// User Name disablen
        userName.setFocusable(false);
        
        viewBtnCancelChange = (View)findViewById(R.id.btnCancelChange);
        viewBtnCancelChange.setVisibility(View.GONE);
        
        btnChange = (Button)findViewById(R.id.btnChange);
        btnChange.setText(getResources().getString(R.string.btnChange));
        
        previousUserName = ProfileManager.getInstance().getProfile().getName();
        userName.setText(previousUserName);
    }
	
	@Override
	protected void onResume()
	{
		super.onResume();
	}
	
	public void btnChangeOnClick(View v) {
		
		if (btnChange.getText() == getResources().getString(R.string.btnChange)) {	// Name änderbar machen
			userName.setEnabled(true);		// User Name enablen
	        userName.setFocusableInTouchMode(true);
	        viewBtnCancelChange.setVisibility(View.VISIBLE);
	        btnChange.setText(getResources().getString(R.string.btnSave));
		}
		else {	// Geänderten Namen speichern
			
			// geänderten Namen speichern
			if (ValidateHelper.isValidUserName(userName.getText().toString()))
			{
				userName.setEnabled(false);		// User Name disablen
		        userName.setFocusable(false);
		    	viewBtnCancelChange.setVisibility(View.GONE);
		    	btnChange.setText(getResources().getString(R.string.btnChange));
		    	
		    	int userId = ProfileManager.getInstance().getProfile().getUserId();
		    	if (userId > 0) // already logged in -> so terminate user
		    	{
		    		ComTerminate terminateObject = new ComTerminate();
		    		terminateObject.setUserId(userId);
		    		TerminateAsync terminator = new TerminateAsync(this);
		    		terminator.execute();
		    	}
		    	
				ComLogin loginObject = new ComLogin();
				loginObject.setUserName(userName.getText().toString());
				AsyncTaskSendReceive<ComLogin, ComLogin> loginTask = new AsyncTaskSendReceive<ComLogin, ComLogin>(ComLogin.class, this, loginObject);
				loginTask.execute();
			}
			else
			{
				Toast.makeText(this, R.string.invalidUserName, Toast.LENGTH_SHORT).show();
			}
		}
    }
	
    public void btnCancelChangeOnClick(View v) {
    	userName.setEnabled(false);		// User Name disablen
        userName.setFocusable(false);
        userName.setText(previousUserName);
    	viewBtnCancelChange.setVisibility(View.GONE);
    	btnChange.setText(getResources().getString(R.string.btnChange));
    }

	@Override
	public void receivedOkResult(ComLogin resultObject, ParcelableSocket socket) {
		
		ProfileManager.getInstance().getProfile().setName(resultObject.getUserName());
		ProfileManager.getInstance().getProfile().setUserId(resultObject.getUserId()); // is important that the login will be executed again
	}

	@Override
	public void receivedError(ComError errorObject) {
		LOGGER.severe("could not login to server");
		LOGGER.severe("ERROR-Code: " + errorObject.getErrorCode());
		LOGGER.severe("ERROR-Msg: " + errorObject.getError());
	}
}
