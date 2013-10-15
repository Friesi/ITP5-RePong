package at.frikiteysch.repong;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class ActivityJoinGame extends Activity {

	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_game);
    }
	
	public void btnRefreshOnClick(View v) {
    	// TODO: noch zu implementieren
    }
	
	public void btnBackOnClick(View v) {
		ActivityJoinGame.super.onBackPressed();
    }
}
