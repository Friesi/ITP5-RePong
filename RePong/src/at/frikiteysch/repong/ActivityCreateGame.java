package at.frikiteysch.repong;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import at.frikiteysch.repong.communication.TerminateAsync;
import at.frikiteysch.repong.helper.ValidateHelper;

public class ActivityCreateGame extends Activity {

	private int actPlayerCount = 0;
	
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
		    	actPlayerCount = progress + 1;	// +1 weil progress immer von 0 weggeht
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
			Intent myIntent = new Intent(this, ActivityGame.class);	// TODO: noch auszutauschen
			// TODO: Parameter �bergeben damit erkannt wird das es sich um den Practice Mode handelt
			this.startActivity(myIntent);
		}
		else	// Normal Mode
		{
			String gameName = ((EditText)findViewById(R.id.txtGamename)).getText().toString();
			
			if (ValidateHelper.isValidGameName(gameName))
			{
				Intent myIntent = new Intent(this, ActivityWaitingRoom.class);
				this.startActivity(myIntent);
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
}
