package at.frikiteysch.repong.defines;

import java.io.Serializable;

/**
 * This class provides a Position consisting of a given x and y.
 */
public class Position implements Serializable{
	
	private static final long serialVersionUID = -5363230091170682011L;
	
	private int x;
	private int y;
	
	public Position(int x, int y)
	{
		this.x = x;
		this.y = y;
	}
	
	public Position()
	{
		this(0,0);
	}
	
	public int getX() {
		return x;
	}
	public void setX(int x) {
		this.x = x;
	}
	public int getY() {
		return y;
	}
	public void setY(int y) {
		this.y = y;
	}
	
}
