package at.frikiteysch.repong.defines;

import java.io.Serializable;

/**
 * This class provides a Field consisting of a given width and height.
 */
public class Field implements Serializable{
	private static final long serialVersionUID = -6718874644704647179L;
	private double height;
	private double width;
	
	public double getHeight() {
		return height;
	}
	public void setHeight(double height) {
		this.height = height;
	}
	public double getWidth() {
		return width;
	}
	public void setWidth(double width) {
		this.width = width;
	}
}
