package at.frikiteysch.repong;

import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class ActivityJoinGame extends Activity {
	private Map<Integer, GameListInfo> gameList;
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_game);
        
        gameList = sendGameListRequest();
    }
	

	public void btnRefreshOnClick(View v) {
    	// TODO: noch zu implementieren
    }
	
	public void btnBackOnClick(View v) {
		ActivityJoinGame.super.onBackPressed();
    }

	private Map<Integer, GameListInfo> sendGameListRequest() {
		// TODO Auto-generated method stub
		return null;
	}
}
