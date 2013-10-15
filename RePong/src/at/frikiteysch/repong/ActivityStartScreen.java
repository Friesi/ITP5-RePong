package at.frikiteysch.repong;

import android.os.Bundle;
import android.app.Activity;
import android.app.FragmentManager;
import android.content.Intent;
import android.view.Menu;
import android.view.View;

public class ActivityStartScreen extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_screen);
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
    public void btnCreateGameOnClick(View v) {
    	Intent myIntent = new Intent(this, ActivityCreateGame.class);
        this.startActivity(myIntent);
    }
    
    public void btnJoinGameOnClick(View v) {
    	Intent myIntent = new Intent(this, ActivityJoinGame.class);
        this.startActivity(myIntent);
    }
    
    public void btnProfileOnClick(View v) {
    	Intent myIntent = new Intent(this, ActivityProfile.class);
        this.startActivity(myIntent);
    }
    
    
    
    // Nur als Test : TODO: auslagern
    public void btnGameStartOnClick(View v) {
    	Intent myIntent = new Intent(this, MovePaddle.class);
        //myIntent.putExtra("key", value); //Optional parameters
        this.startActivity(myIntent);
    }
    
}
