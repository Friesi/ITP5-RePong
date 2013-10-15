package at.frikiteysch.repong;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ActivityProfile extends Activity {

	private TextView userName;
	private Button btnChange;
	private View viewBtnCancelChange;
	
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
        
        // TODO: UserName aus Datenbank auslesen und in TextView reinschreiben
    }
	
	public void btnChangeOnClick(View v) {
		
		if (btnChange.getText() == getResources().getString(R.string.btnChange)) {	// Name änderbar machen
			userName.setEnabled(true);		// User Name enablen
	        userName.setFocusableInTouchMode(true);
	        viewBtnCancelChange.setVisibility(View.VISIBLE);
	        btnChange.setText(getResources().getString(R.string.btnSave));
		}
		else {	// Geänderten Namen speichern
			
			
			
			// TODO: geänderten Namen speichern
			
			
			
			userName.setEnabled(false);		// User Name disablen
	        userName.setFocusable(false);
	    	viewBtnCancelChange.setVisibility(View.GONE);
	    	btnChange.setText(getResources().getString(R.string.btnChange));
		}
    }
	
    public void btnCancelChangeOnClick(View v) {
    	userName.setEnabled(false);		// User Name disablen
        userName.setFocusable(false);
        userName.setText("");
    	viewBtnCancelChange.setVisibility(View.GONE);
    	btnChange.setText(getResources().getString(R.string.btnChange));
    	
    	// TODO: alten UserName aus Datenbank wieder hineinschreiben
    }
    
	public void btnBackOnClick(View v) {
		ActivityProfile.super.onBackPressed();
    }
}
