package at.frikiteysch.repong;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import at.frikiteysch.repong.communication.AsyncTaskSendReceive;
import at.frikiteysch.repong.communication.ParcelableSocket;
import at.frikiteysch.repong.communication.TerminateAsync;
import at.frikiteysch.repong.communication.AsyncTaskSendReceive.AsyncTaskStateReceiver;
import at.frikiteysch.repong.listview.WaitingRoomArrayAdapter;
import at.frikiteysch.repong.storage.ProfileManager;

public class ActivityWaitingRoom extends Activity implements AsyncTaskStateReceiver<ComWaitInfo> {

	ParcelableSocket gameSocket;
	ComWaitInfo waitInfo;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.activity_waiting_room);
	    
	    Intent intent = getIntent();
	    
	    Boolean isCreator = intent.getBooleanExtra("isCreator", false);
	    gameSocket = intent.getParcelableExtra("socket");
	    waitInfo = (ComWaitInfo) intent.getSerializableExtra("waitInfo");
	    
	    if (!isCreator) {
		    View btnStart = (View) findViewById(R.id.btnStart);	
		    btnStart.setVisibility(View.GONE);
	    }
	    else {
		    View lblPleaseWait = (View) findViewById(R.id.lblPleaseWait);
		    lblPleaseWait.setVisibility(View.GONE);
	    }
	    
	    ListView lv = (ListView) findViewById(R.id.listViewPlayers);
	    
	    
	    // TODO: echte spieler in liste übergeben 
	    WaitingRoomArrayAdapter arrayAdapter = new WaitingRoomArrayAdapter(this, new String[] {"Spieler 1", "Spieler 2", "Spieler 3", "Spieler 4"});
	    lv.setAdapter(arrayAdapter);
	}
	
	public void btnStartOnClick(View v) {
    	// TODO: noch zu implementieren
    }
	
	public void btnLeaveOnClick(View v) {
		
		ComLeaveGame leaveGame = new ComLeaveGame();
		leaveGame.setUserId(ProfileManager.getInstance().getProfile().getUserId());
		
		AsyncTaskSendReceive<ComLeaveGame, ComWaitInfo> task = 
    			new AsyncTaskSendReceive<ComLeaveGame, ComWaitInfo>(ComWaitInfo.class, this, leaveGame);
    	
		task.execute();
    }

	@Override
	public void receivedOkResult(ComWaitInfo resultObject, ParcelableSocket socket) {
		// no return value possible
	}

	@Override
	public void receivedError(ComError errorObject) {
		// no error possible -> go to start activity
		Intent myIntent = new Intent(this, ActivityStartScreen.class);
		myIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		this.startActivity(myIntent);
	}
}
