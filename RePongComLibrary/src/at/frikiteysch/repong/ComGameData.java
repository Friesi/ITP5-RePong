package at.frikiteysch.repong;

import java.awt.Dimension;
import java.io.Serializable;
import java.util.ArrayList;

public class ComGameData  implements Serializable {

	private static final long serialVersionUID = 12L;
	
	private Ball ball;
	private Dimension field;
	private ArrayList<Player> playerList;
	
	public Ball getBall() {
		return ball;
	}
	public void setBall(Ball ball) {
		this.ball = ball;
	}
	public Dimension getField() {
		return field;
	}
	public void setField(Dimension field) {
		this.field = field;
	}
	public ArrayList<Player> getPlayerList() {
		return playerList;
	}
	public void setPlayerList(ArrayList<Player> playerList) {
		this.playerList = playerList;
	}
}
