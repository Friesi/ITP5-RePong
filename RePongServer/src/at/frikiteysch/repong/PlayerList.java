package at.frikiteysch.repong;

import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/** PlayerList - Singleton - Thread Save */

public class PlayerList {
	private static PlayerList instance = new PlayerList(); // better readable
	private static AtomicInteger lastId = new AtomicInteger();
	private static final ConcurrentMap<Integer, PlayerInfo> playerList = new ConcurrentHashMap<Integer, PlayerInfo>();
	private final Lock lock = new ReentrantLock();
	
    private PlayerList() { }

    public static PlayerList getInstance() {
    	return instance;
    }
    
    public ConcurrentMap<Integer, PlayerInfo> getPlayerList() {
    	return playerList;
    }
    
    /**
     * Thread safe method for generating an id for player
     * @param login
     * @param s
     */
    public void generateIdForPlayer(ComLogin login, Socket s) {
    	PlayerInfo playerInfo = new PlayerInfo();
    	playerInfo.name = login.getUserName();
    	playerInfo.timeStamp = System.currentTimeMillis();
    	playerInfo.s = s;
    	
    	lock.lock(); // adding id thread safe - also set ComLogin Return Object id
    	playerList.putIfAbsent(lastId.incrementAndGet(), playerInfo);

    	login.setUserId(lastId.get());
    	lock.unlock();
    }
}
