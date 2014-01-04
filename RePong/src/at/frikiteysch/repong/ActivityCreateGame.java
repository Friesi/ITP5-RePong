package at.frikiteysch.repong;

import java.util.logging.Logger;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import at.frikiteysch.repong.communication.AsyncTaskSendReceive;
import at.frikiteysch.repong.communication.AsyncTaskSendReceive.AsyncTaskStateReceiver;
import at.frikiteysch.repong.helper.ValidateHelper;
import at.frikiteysch.repong.storage.ProfileManager;

public class ActivityCreateGame extends Activity implements AsyncTaskStateReceiver<ComWaitInfo> {

	private int actPlayerCount = 2;
	private static Logger LOGGER = Logger.getLogger(ActivityStartScreen.class.getName());
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_game);
        
        SeekBar seekBar = (SeekBar)findViewById(R.id.seekBarPlayerCount); 
        final TextView seekBarValue = (TextView)findViewById(R.id.lblSeekValue);
        seekBarValue.setText("2");

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener(){ 
		    @Override 
		    public void onProgressChanged(SeekBar seekBar, int progress, 
		      boolean fromUser) { 
		    	actPlayerCount = progress + 1;	// +1 because progress starts at 0
		    	seekBarValue.setText(String.valueOf(actPlayerCount));
		    } 
		
		    @Override 
		    public void onStartTrackingTouch(SeekBar seekBar) { 
		    	// TODO Auto-generated method stub 
		    } 
		
		    @Override 
		    public void onStopTrackingTouch(SeekBar seekBar) { 
		    	// TODO Auto-generated method stub 
		    }
        });
    }
	
	public void btnCreateOnClick(View v) {
    	// TODO: noch zu implementieren
		
		if (actPlayerCount == 1)	// Practice Mode
		{
			//send createGame object to server with asynctask
	    	ComCreateGame createGame = new ComCreateGame();
	    	createGame.setCreatorId(ProfileManager.getInstance().getProfile().getUserId());
	    	createGame.setMaxPlayerCount(actPlayerCount);
	    	createGame.setGameName("");
	    	
	    	AsyncTaskSendReceive<ComCreateGame, ComWaitInfo> task = 
	    			new AsyncTaskSendReceive<ComCreateGame, ComWaitInfo>(ComWaitInfo.class, this, createGame);
	    	
			task.execute();
			
			Toast.makeText(this, R.string.createGameInProgress, Toast.LENGTH_LONG).show();
			Button btnCreate = (Button)findViewById(R.id.btnCreate);
			btnCreate.setEnabled(false);
		}
		else	// Normal Mode
		{
			String gameName = ((EditText)findViewById(R.id.txtGamename)).getText().toString();
			
			if (ValidateHelper.isValidGameName(gameName))
			{
				//send createGame object to server with asynctask
		    	ComCreateGame createGame = new ComCreateGame();
		    	createGame.setCreatorId(ProfileManager.getInstance().getProfile().getUserId());
		    	createGame.setMaxPlayerCount(actPlayerCount);
		    	createGame.setGameName(gameName);
				
		    	AsyncTaskSendReceive<ComCreateGame, ComWaitInfo> task = 
		    			new AsyncTaskSendReceive<ComCreateGame, ComWaitInfo>(ComWaitInfo.class, this, createGame);
		    	
				task.execute();
				
				Toast.makeText(this, R.string.createGameInProgress, Toast.LENGTH_LONG).show();
				Button btnCreate = (Button)findViewById(R.id.btnCreate);
				btnCreate.setEnabled(false);
			}
			else	// Show Error Msg
			{
				Toast.makeText(this, R.string.invalidGameName, Toast.LENGTH_SHORT).show();
			}
		}
    }
	
	public void btnCancelOnClick(View v) {
		ActivityCreateGame.super.onBackPressed();
    }

	@Override
	public void receivedOkResult(ComWaitInfo resultObject) {
		if (resultObject.getMaxPlayerCount() != 1) {
			Intent myIntent = new Intent(this, ActivityWaitingRoom.class);
			myIntent.putExtra("isCreator", true);
			myIntent.putExtra("waitInfo", resultObject);
			this.startActivity(myIntent);
		}
		else { // Practice Mode
			Intent intent = new Intent(this, ActivityGame.class);
	    	intent.putExtra("gameId", resultObject.getGameId());
	    	this.startActivity(intent);
		}
	}

	@Override
	public void receivedError(ComError errorObject) {
		LOGGER.severe("could not create game");
		LOGGER.severe("ERROR-Code: " + errorObject.getErrorCode());
		LOGGER.severe("ERROR-Msg: " + errorObject.getError());
	}
}
