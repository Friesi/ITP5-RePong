package at.frikiteysch.repong;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import at.frikiteysch.repong.communication.CommunicationCenter;
import at.frikiteysch.repong.communication.TerminateAsync;

public class ActivityJoinGame extends Activity {
	private Map<Integer, GameListInfo> gameList;
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_game);
        
        gameList = getGameListRequest();
    }
	

	public void btnRefreshOnClick(View v) {
    	// TODO: noch zu implementieren
    }
	
	public void btnBackOnClick(View v) {
		ActivityJoinGame.super.onBackPressed();
    }

	public Map<Integer, GameListInfo> getGameListRequest() {
		ComRefreshGameList comRefreshGameList = new ComRefreshGameList();
		int userId =0; //TODO obtain correct userId !!!!!!!!!!!
		comRefreshGameList.setUserId(userId);
		
		Socket s;
		try {
			s = new Socket(CommunicationCenter.serverAddress, CommunicationCenter.serverPort);
		
			CommunicationCenter.sendComObjectToServer(s, comRefreshGameList);
			
			ComGameList comGameList = (ComGameList) CommunicationCenter.recieveComObjectFromServer(s);
			
			return comGameList.getGameListInfo();
		
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}	
}
