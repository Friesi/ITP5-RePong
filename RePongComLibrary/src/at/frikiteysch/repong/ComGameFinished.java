package at.frikiteysch.repong;

import java.io.Serializable;
import java.util.List;

public class ComGameFinished  implements Serializable {

	private static final long serialVersionUID = 14L;
	
	private List<String> ranking;

	public List<String> getRanking() {
		return ranking;
	}

	public void setRanking(List<String> ranking) {
		this.ranking = ranking;
	}
}
