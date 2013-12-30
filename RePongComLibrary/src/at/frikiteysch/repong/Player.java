package at.frikiteysch.repong;

import java.io.Serializable;

import at.frikiteysch.repong.defines.RePongDefines.PaddleOrientation;

//import java.awt.ComponentOrientation;

public class Player implements Serializable{
	private static final long serialVersionUID = 7395014220314522713L;
	
	private PaddleOrientation orientation;
	private int color;
	private int lifes;
	private int position;
	private int width;
	private int userId;
	private String name;

	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public PaddleOrientation getOrientation() {
		return orientation;
	}
	public void setOrientation(PaddleOrientation orientation) {
		this.orientation = orientation;
	}
	public int getColor() {
		return color;
	}
	public void setColor(int color) {
		this.color = color;
	}
	public int getLifes() {
		return lifes;
	}
	public void setLifes(int lifes) {
		this.lifes = lifes;
	}
	public int getPosition() {
		return position;
	}
	public void setPosition(int position) {
		this.position = position;
	}
	public int getWidth() {
		return width;
	}
	public void setWidth(int width) {
		this.width = width;
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
}
