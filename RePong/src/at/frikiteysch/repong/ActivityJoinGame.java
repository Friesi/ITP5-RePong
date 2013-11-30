package at.frikiteysch.repong;

import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import at.frikiteysch.repong.communication.AsyncTaskSendReceive.AsyncTaskStateReceiver;
import at.frikiteysch.repong.communication.AsyncTaskSendReceive;
import at.frikiteysch.repong.communication.CommunicationCenter;
import at.frikiteysch.repong.communication.TerminateAsync;
import at.frikiteysch.repong.storage.ProfileManager;

public class ActivityJoinGame extends Activity implements AsyncTaskStateReceiver<ComGameList> {
	private final Activity activity = this;
	
	private Map<Integer, GameListInfo> gameList;
	//LIST OF ARRAY STRINGS WHICH WILL SERVE AS LIST ITEMS
    ArrayList<String> listItems=new ArrayList<String>();

    //DEFINING A STRING ADAPTER WHICH WILL HANDLE THE DATA OF THE LISTVIEW
    ArrayAdapter<String> adapter;
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_game);
  
        startGameListRequest();
        ListView listView = (ListView) findViewById(R.id.listView1);
        adapter=new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1,
                listItems);
        listView.setAdapter(adapter);
        
        listView.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                    int position, long id) {
            	
            	String str = listItems.get((int)id);
            	//Toast.makeText(view.getContext(),String.valueOf(id)+" "+str,Toast.LENGTH_SHORT).show();
            	str =String.valueOf(str.subSequence(str.indexOf("[")+1,str.indexOf("]")) );
             	Toast.makeText(view.getContext(),str,Toast.LENGTH_SHORT).show();
             	int gameIdToJoin = Integer.parseInt(str);
                //TODO join game with gameIdToJoin 
             	
             	ComWaitInfo waitInfo = new ComWaitInfo();
             	waitInfo.setGameId(gameIdToJoin);
             	
             	Intent myIntent = new Intent(activity.getBaseContext(), ActivityWaitingRoom.class);
        		myIntent.putExtra("isCreator", false);
        		myIntent.putExtra("waitInfo", waitInfo);
        		startActivity(myIntent);
            }
        });
    }
	

	public void btnRefreshOnClick(View v) {
    	startGameListRequest();
    }
	
	public void btnBackOnClick(View v) {
		ActivityJoinGame.super.onBackPressed();
    }

	public void startGameListRequest() {
		ComRefreshGameList comRefreshGameList = new ComRefreshGameList();
		int userId = ProfileManager.getInstance().getProfile().getUserId();
		comRefreshGameList.setUserId(userId);
			
			//send comRefreshGameList object to server with asynctask
	    	AsyncTaskSendReceive<ComRefreshGameList, ComGameList> task = 
	    			new AsyncTaskSendReceive<ComRefreshGameList, ComGameList>(ComGameList.class, this, comRefreshGameList);

			task.execute();
	
	}


	@Override
	public void receivedOkResult(ComGameList resultObject) {
		this.gameList=resultObject.getGameListInfo();
		RefreshList();
		if(gameList.size()==0){
			Toast.makeText(getApplicationContext(), "Keine Spiele verfügbar!", Toast.LENGTH_LONG).show();
		}
		
	}



	private void RefreshList() {
		listItems.clear();
		for(GameListInfo gameInfo : gameList.values()){
			listItems.add("["+gameInfo.getGameId()+"] ["+gameInfo.getCurPlayerCount()+"/"+gameInfo.getMaxPlayerCount()+"] "+gameInfo.getGameName());
		}
		adapter.notifyDataSetChanged();
		
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
