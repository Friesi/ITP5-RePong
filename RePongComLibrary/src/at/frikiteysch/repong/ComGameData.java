package at.frikiteysch.repong;

import java.awt.geom.Dimension2D;
import java.io.Serializable;
import java.util.ArrayList;

public class ComGameData  implements Serializable {

	private static final long serialVersionUID = 12L;
	
	private Ball ball;
	private Dimension2D field;
	private ArrayList<Player> playerList;
	
	public Ball getBall() {
		return ball;
	}
	public void setBall(Ball ball) {
		this.ball = ball;
	}
	public Dimension2D getField() {
		return field;
	}
	public void setField(Dimension2D field) {
		this.field = field;
	}
	public ArrayList<Player> getPlayerList() {
		return playerList;
	}
	public void setPlayerList(ArrayList<Player> playerList) {
		this.playerList = playerList;
	}
}
