package at.frikiteysch.repong;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/** PlayerList - Singleton - Thread Save */

public class PlayerList {
	private static ConcurrentMap<Integer, PlayerInfo> instance = null;
	
    private PlayerList() {
    	
    }

    public static ConcurrentMap<Integer, PlayerInfo> getInstance() {
            if (instance == null) {
                    synchronized (PlayerList.class){
                            if (instance == null) {
                                    instance = new ConcurrentHashMap<Integer, PlayerInfo>();
                            }
                    }
            }
            return instance;
    }
    
    public int generateIdForPlayer() {
    	int id = 0;
    	
    	
    	
    	return id;
    }
}
