package at.frikiteysch.repong;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import at.frikiteysch.repong.communication.TerminateAsync;

public class ActivityWaitingRoom extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.activity_waiting_room);
	    
	    
	    // TODO: bei allen anderen Spielern ausblenden
	    //View btnStart = (View) findViewById(R.id.btnStart);	
	    //btnStart.setVisibility(View.GONE);
	    
	    View lblPleaseWait = (View) findViewById(R.id.lblPleaseWait);
	    lblPleaseWait.setVisibility(View.GONE);
	}
	
	public void btnStartOnClick(View v) {
    	// TODO: noch zu implementieren
    }
	
	public void btnLeaveOnClick(View v) {
		ActivityWaitingRoom.super.onBackPressed();
    }
}
