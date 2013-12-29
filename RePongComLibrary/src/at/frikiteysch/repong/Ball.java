package at.frikiteysch.repong;

import java.io.Serializable;

import at.frikiteysch.repong.defines.Position;

public class Ball implements Serializable{
	private static final long serialVersionUID = -5746123511070826647L;
	
	private int color;
	private int size;
	private Position position;
	
	public int getColor() {
		return color;
	}
	public void setColor(int color) {
		this.color = color;
	}
	public int getSize() {
		return size;
	}
	public void setSize(int size) {
		this.size = size;
	}
	public Position getPosition() {
		return position;
	}
	public void setPosition(Position position) {
		this.position = position;
	}
}
