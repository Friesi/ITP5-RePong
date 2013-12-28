package at.frikiteysch.repong;

import java.io.Serializable;
import java.util.ArrayList;

import at.frikiteysch.repong.defines.Field;

public class ComGameData  implements Serializable {

	private static final long serialVersionUID = 12L;
	
	
	private Ball ball;
	private Field field;
	private ArrayList<Player> playerList;
	
	public Ball getBall() {
		return ball;
	}
	public void setBall(Ball ball) {
		this.ball = ball;
	}
	public Field getField() {
		return field;
	}
	public void setField(Field field) {
		this.field = field;
	}
	public ArrayList<Player> getPlayerList() {
		return playerList;
	}
	public void setPlayerList(ArrayList<Player> playerList) {
		this.playerList = playerList;
	}
}
