package at.frikiteysch.repong;

import java.util.logging.Logger;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.view.View;
import android.widget.ListView;
import at.frikiteysch.repong.communication.AsyncTaskSend;
import at.frikiteysch.repong.communication.AsyncTaskSendReceive;
import at.frikiteysch.repong.listview.WaitingRoomArrayAdapter;
import at.frikiteysch.repong.services.WaitingRoomGetComWaitInfo;
import at.frikiteysch.repong.storage.ProfileManager;

public class ActivityWaitingRoom extends Activity {
	private ListView listViewPlayers;
	
	private Intent getComWaitInfoIntent;
	ComWaitInfo waitInfo;
	int gameId;
	
	private static Logger LOGGER = Logger.getLogger(ActivityStartScreen.class.getName());
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.activity_waiting_room);
	    
	    Intent intent = getIntent();
	    
	    Boolean isCreator = intent.getBooleanExtra("isCreator", false);
	    waitInfo = (ComWaitInfo) intent.getSerializableExtra("waitInfo");
	    int maxPlayerCnt = waitInfo.getMaxPlayerCount();
	    gameId = waitInfo.getGameId();
	    
	    if (!isCreator) {
		    View btnStart = (View) findViewById(R.id.btnStart);	
		    btnStart.setVisibility(View.GONE);
	    }
	    else {
		    View lblPleaseWait = (View) findViewById(R.id.lblPleaseWait);
		    lblPleaseWait.setVisibility(View.GONE);
	    }
	    
	    listViewPlayers = (ListView) findViewById(R.id.listViewPlayers);
	    
	    String[] players = new String[] {"", "", "", ""};
	    
	    WaitingRoomArrayAdapter arrayAdapter = new WaitingRoomArrayAdapter(this, players, maxPlayerCnt);
	    listViewPlayers.setAdapter(arrayAdapter);
	    
	    getComWaitInfoIntent = new Intent(this, WaitingRoomGetComWaitInfo.class);
	    
	    if (!isServiceRunning(WaitingRoomGetComWaitInfo.class.getName()) && waitInfo != null) {
    	   getComWaitInfoIntent.putExtra(getString(R.string.ComWaitInfoIntentGameId), waitInfo.getGameId());
    	   startService(getComWaitInfoIntent);
	    }
	}
	
	public void btnStartOnClick(View v) {
    	// TODO: noch zu implementieren
		
		// service beenden nicht vergessen!!!! 
    }
	
	 @Override
	 public void onBackPressed() {
		 btnLeaveOnClick(null);
	 }
	
	public void btnLeaveOnClick(View v) {
		stopService(getComWaitInfoIntent);
		
		ComLeaveGame leaveGame = new ComLeaveGame();
		leaveGame.setUserId(ProfileManager.getInstance().getProfile().getUserId());
		leaveGame.setGameId(gameId);
		
		AsyncTaskSend<ComLeaveGame> task = new AsyncTaskSend<ComLeaveGame>(leaveGame);
		task.execute();
		
		// go to start activity
		Intent myIntent = new Intent(this, ActivityStartScreen.class);
		myIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		this.startActivity(myIntent);
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
	
	protected void onResume() {
       super.onResume();
       
       if (!isServiceRunning(WaitingRoomGetComWaitInfo.class.getName()) && waitInfo != null) {
    	   getComWaitInfoIntent.putExtra(getString(R.string.ComWaitInfoIntentGameId), waitInfo.getGameId());
    	   startService(getComWaitInfoIntent);
       }
       
       LocalBroadcastManager.getInstance(this).registerReceiver(receiver,
     	      new IntentFilter(WaitingRoomGetComWaitInfo.WAIT_INFO_RESULT));
	}
	
	private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String[] players = intent.getStringArrayExtra(WaitingRoomGetComWaitInfo.WAIT_INFO_RESULT_PLAYERS);
            int playerCnt = intent.getIntExtra(WaitingRoomGetComWaitInfo.WAIT_INFO_RESULT_PLAYERCNT, 0);

            WaitingRoomArrayAdapter arrayAdapter = new WaitingRoomArrayAdapter(context, players, playerCnt);
            listViewPlayers.setAdapter(arrayAdapter);
        }
    };
	
	@Override
    protected void onPause() {
		super.onPause();
      	LocalBroadcastManager.getInstance(this).unregisterReceiver(receiver);
    }
}
