package at.frikiteysch.repong;

import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import at.frikiteysch.repong.communication.AsyncTaskSendReceive.AsyncTaskStateReceiver;
import at.frikiteysch.repong.communication.AsyncTaskSendReceive;
import at.frikiteysch.repong.communication.CommunicationCenter;
import at.frikiteysch.repong.communication.ParcelableSocket;
import at.frikiteysch.repong.communication.TerminateAsync;
import at.frikiteysch.repong.storage.ProfileManager;

public class ActivityJoinGame extends Activity implements AsyncTaskStateReceiver<ComGameList> {
	private Map<Integer, GameListInfo> gameList;
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_game);
  
        startGameListRequest();
    }
	

	public void btnRefreshOnClick(View v) {
    	// TODO: noch zu implementieren
    }
	
	public void btnBackOnClick(View v) {
		ActivityJoinGame.super.onBackPressed();
    }

	public void startGameListRequest() {
		ComRefreshGameList comRefreshGameList = new ComRefreshGameList();
		int userId = 0;//ProfileManager.getInstance().getProfile().getUserId();
		comRefreshGameList.setUserId(userId);
			
			//send comRefreshGameList object to server with asynctask
	    	AsyncTaskSendReceive<ComRefreshGameList, ComGameList> task = 
	    			new AsyncTaskSendReceive<ComRefreshGameList, ComGameList>(ComGameList.class, this, comRefreshGameList);

			task.execute();
	
	}


	@Override
	public void receivedOkResult(ComGameList resultObject, ParcelableSocket socket) {
		this.gameList=resultObject.getGameListInfo();
		
	}



	@Override
	public void receivedError(ComError errorObject) {
		this.gameList=null;
		
	}	
	


	public Map<Integer, GameListInfo> getGameList() {
		return gameList;
	}


	public void setGameList(Map<Integer, GameListInfo> gameList) {
		this.gameList = gameList;
	}
}
