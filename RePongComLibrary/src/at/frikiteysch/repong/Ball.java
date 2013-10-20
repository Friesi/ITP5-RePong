package at.frikiteysch.repong;

import java.awt.geom.Point2D;

public class Ball {
	private int color;
	private int size;
	private Point2D position;
	
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
	public Point2D getPosition() {
		return position;
	}
	public void setPosition(Point2D position) {
		this.position = position;
	}
}
