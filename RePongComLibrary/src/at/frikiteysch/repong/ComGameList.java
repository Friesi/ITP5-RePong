package at.frikiteysch.repong;

import java.io.Serializable;
import java.util.Map;

public class ComGameList  implements Serializable {

	private static final long serialVersionUID = 10L;
	
	private Map<Integer, GameListInfo> gameListInfo;

	public Map<Integer, GameListInfo> getGameListInfo() {
		return gameListInfo;
	}

	public void setGameListInfo(Map<Integer, GameListInfo> gameListInfo) {
		this.gameListInfo = gameListInfo;
	}
}
