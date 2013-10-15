package at.frikiteysch.repong;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class ActivityFirstStartScreen extends Activity {

	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_screen);
    }
	
	public void btnOkOnClick(View v) {
		// TODO: noch zu implementieren
    }
	
	public void btnExitOnClick(View v) {
		finish();
    }
}
