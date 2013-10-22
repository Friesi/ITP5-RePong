package at.frikiteysch.repong;

import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;

/** PlayerList - Singleton - Thread Save */

public class PlayerList {
	private static PlayerList instance = null;
	private static AtomicInteger lastId = new AtomicInteger();
	private static final ConcurrentMap<Integer, PlayerInfo> playerList = new ConcurrentHashMap<Integer, PlayerInfo>();
	
    private PlayerList() { }

    public static PlayerList getInstance() {
            if (instance == null) {
                    synchronized (PlayerList.class){
                            if (instance == null) {
                                    instance = new PlayerList();
                            }
                    }
            }
            return instance;
    }
    
    public ConcurrentMap<Integer, PlayerInfo> getPlayerList() {
    	return playerList;
    }
    
    public void generateIdForPlayer(ComLogin login, Socket s) {
    	PlayerInfo playerInfo = new PlayerInfo();
    	playerInfo.name = login.getUserName();
    	playerInfo.timeStamp = System.currentTimeMillis();
    	playerInfo.s = s;
    	
    	playerList.putIfAbsent(lastId.incrementAndGet(), playerInfo);
    	
    	login.setUserId(lastId.get());
    }
}
