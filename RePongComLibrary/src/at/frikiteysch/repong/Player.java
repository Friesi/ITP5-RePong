package at.frikiteysch.repong;

import java.awt.ComponentOrientation;

public class Player {
	private Enum<ComponentOrientation> orientation;
	private int color;
	private int lifes;
	private int position;
	private int userId;

	public Enum<ComponentOrientation> getOrientation() {
		return orientation;
	}
	public void setOrientation(Enum<Orientation> orientation) {
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
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
}
