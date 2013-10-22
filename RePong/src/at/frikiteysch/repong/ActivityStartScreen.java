package at.frikiteysch.repong;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.Toast;
import at.frikiteysch.repong.ComLogin;

public class ActivityStartScreen extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_screen);
        
        SendObjectAsync task = new SendObjectAsync();
		task.execute();
    }
    
    private class SendObjectAsync extends AsyncTask<Void, Void, ComLogin> {

		@Override
		protected ComLogin doInBackground(Void... args) {

			ComLogin comLoginObject = null;
			
			try {
		        ComLogin objectToSend = new ComLogin();
		        objectToSend.setUserName("blubbbbb");
		        Socket s = new Socket("10.0.2.2", 3456);	//"ec2-54-200-186-85.us-west-2.compute.amazonaws.com", 3456);
		        ObjectOutputStream out = new ObjectOutputStream(s.getOutputStream());
		        out.writeObject(objectToSend);
		        out.flush();
		        
		        
		        // Answer from server
		        ObjectInputStream inputObject = new ObjectInputStream(s.getInputStream());
		        comLoginObject = (ComLogin) inputObject.readObject();;
		        
	        } catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			return comLoginObject;
		}

		@Override
		protected void onPostExecute(ComLogin result) {
			if (result != null)
				Toast.makeText(ActivityStartScreen.this, "PlayerId: " + result.getUserId(), Toast.LENGTH_LONG).show();
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
