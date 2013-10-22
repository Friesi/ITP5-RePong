package at.frikiteysch.repong;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import at.frikiteysch.repong.ComLogin;

public class ActivityStartScreen extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_screen);
        
        SenObjectAsync task = new SenObjectAsync();
		task.execute();
    }
    
    private class SenObjectAsync extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... args) {

			try {
		        ComLogin objectToSend = new ComLogin();
		        objectToSend.setUserName("blubbbbb");
		        Socket s = new Socket("10.0.2.2", 3456);	//"ec2-54-200-186-85.us-west-2.compute.amazonaws.com", 3456);
		        ObjectOutputStream out = new ObjectOutputStream(s.getOutputStream());
		        out.writeObject(objectToSend);
		        out.flush();
	        } catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {

		}
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
