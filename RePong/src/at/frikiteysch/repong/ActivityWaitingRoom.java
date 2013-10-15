package at.frikiteysch.repong;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

public class ActivityWaitingRoom extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.activity_waiting_room);
	    
	    View btnStart = findViewById(R.id.btnStart);	// Start-Button vorläufig ausblenden TODO: bei Spielersteller einblenden
	    btnStart.setVisibility(View.GONE);
	}
	
	public void btnStartOnClick(View v) {
    	// TODO: noch zu implementieren
    }
	
	public void btnLeaveOnClick(View v) {
		ActivityWaitingRoom.super.onBackPressed();
    }
	
}
