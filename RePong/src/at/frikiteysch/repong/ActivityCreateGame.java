package at.frikiteysch.repong;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;

public class ActivityCreateGame extends Activity {

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
		    	// TODO Auto-generated method stub 
		    	seekBarValue.setText(String.valueOf(progress + 1));	// +1 weil progress immer von 0 weggeht
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
		
		
		
		Intent myIntent = new Intent(this, ActivityWaitingRoom.class);
        this.startActivity(myIntent);
    }
	
	public void btnCancelOnClick(View v) {
		ActivityCreateGame.super.onBackPressed();
    }
}
