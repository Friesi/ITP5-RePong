package at.frikiteysch.repong;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import at.frikiteysch.repong.storage.ProfileManager;

public class ActivityProfile extends Activity {

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
		
		if (btnChange.getText() == getResources().getString(R.string.btnChange)) {	// Name šnderbar machen
			userName.setEnabled(true);		// User Name enablen
	        userName.setFocusableInTouchMode(true);
	        viewBtnCancelChange.setVisibility(View.VISIBLE);
	        btnChange.setText(getResources().getString(R.string.btnSave));
		}
		else {	// Gešnderten Namen speichern
			
			// gešnderten Namen speichern
			ProfileManager.getInstance().getProfile().setName(userName.getText().toString());
			
			userName.setEnabled(false);		// User Name disablen
	        userName.setFocusable(false);
	    	viewBtnCancelChange.setVisibility(View.GONE);
	    	btnChange.setText(getResources().getString(R.string.btnChange));
		}
    }
	
    public void btnCancelChangeOnClick(View v) {
    	userName.setEnabled(false);		// User Name disablen
        userName.setFocusable(false);
        userName.setText(previousUserName);
    	viewBtnCancelChange.setVisibility(View.GONE);
    	btnChange.setText(getResources().getString(R.string.btnChange));
    }
}
