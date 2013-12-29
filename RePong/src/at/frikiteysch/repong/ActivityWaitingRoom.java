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
import android.widget.Toast;
import at.frikiteysch.repong.communication.AsyncTaskSend;
import at.frikiteysch.repong.communication.AsyncTaskSendReceive;
import at.frikiteysch.repong.communication.AsyncTaskSendReceive.AsyncTaskStateReceiver;
import at.frikiteysch.repong.listview.WaitingRoomArrayAdapter;
import at.frikiteysch.repong.services.WaitingRoomGetComWaitInfo;
import at.frikiteysch.repong.storage.ProfileManager;

public class ActivityWaitingRoom extends Activity implements AsyncTaskStateReceiver<ComGameData>{
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
		stopService(getComWaitInfoIntent);
		
		ComStartGame startGame = new ComStartGame();
		startGame.setGameId(gameId);
		startGame.setUserId(ProfileManager.getInstance().getProfile().getUserId());
		
		AsyncTaskSendReceive<ComStartGame, ComGameData> task = new AsyncTaskSendReceive<ComStartGame, ComGameData>(ComGameData.class, this, startGame);
		task.execute();
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
		
		backToStartActivity();
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
       
       IntentFilter filter = new IntentFilter();
       filter.addAction(WaitingRoomGetComWaitInfo.WAIT_ERROR);
       filter.addAction(WaitingRoomGetComWaitInfo.WAIT_INFO_RESULT);
       filter.addAction(WaitingRoomGetComWaitInfo.GAME_STARTED);
       LocalBroadcastManager.getInstance(this).registerReceiver(receiver,
     	      filter);
	}
	
	private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
        	if (intent.getAction().equals(WaitingRoomGetComWaitInfo.WAIT_INFO_RESULT))
        	{
	            String[] players = intent.getStringArrayExtra(WaitingRoomGetComWaitInfo.WAIT_INFO_RESULT_PLAYERS);
	            int playerCnt = intent.getIntExtra(WaitingRoomGetComWaitInfo.WAIT_INFO_RESULT_PLAYERCNT, 0);
	
	            WaitingRoomArrayAdapter arrayAdapter = new WaitingRoomArrayAdapter(context, players, playerCnt);
	            listViewPlayers.setAdapter(arrayAdapter);
        	}
        	else if (intent.getAction().equals(WaitingRoomGetComWaitInfo.WAIT_ERROR))
        	{
        		stopService(getComWaitInfoIntent);
        		backToStartActivity();
        		showNoCreatorToast();
        	}
        	else if (intent.getAction().equals(WaitingRoomGetComWaitInfo.GAME_STARTED))
        	{
        		LOGGER.info("received game started request ");
        		stopService(getComWaitInfoIntent);
        		startGameActivity();
        	}
        }
    };
    
    private void backToStartActivity()
    {
    	// go to start activity
		Intent myIntent = new Intent(this, ActivityStartScreen.class);
		myIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		this.startActivity(myIntent);
    }
    
    private void startGameActivity()
    {
    	Intent intent = new Intent(this, ActivityGame.class);
    	intent.putExtra("gameId", gameId);
    	this.startActivity(intent);
    }
    
    private void showNoCreatorToast()
    {
    	Toast.makeText(this, "Creator left the game", Toast.LENGTH_SHORT).show();
    }
	
	@Override
    protected void onPause() {
		super.onPause();
      	LocalBroadcastManager.getInstance(this).unregisterReceiver(receiver);
    }

	@Override
	public void receivedOkResult(ComGameData resultObject) {
		startGameActivity();
	}

	@Override
	public void receivedError(ComError errorObject) {
		LOGGER.severe(errorObject.toString());
		Toast.makeText(this, "Could not start game", Toast.LENGTH_SHORT).show();
	}
}
