package at.frikiteysch.repong;

import android.app.Activity;
import android.os.Bundle;
import android.widget.SeekBar;
import android.widget.TextView;

public class ActivityCreateGame extends Activity {

	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_game);
        
        
        SeekBar seekBar = (SeekBar)findViewById(R.id.seekBarPlayerCount); 
        final TextView seekBarValue = (TextView)findViewById(R.id.lblSeekValue); 

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener(){ 
		    @Override 
		    public void onProgressChanged(SeekBar seekBar, int progress, 
		      boolean fromUser) { 
		    	// TODO Auto-generated method stub 
		    	seekBarValue.setText(String.valueOf(progress)); 
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
}
