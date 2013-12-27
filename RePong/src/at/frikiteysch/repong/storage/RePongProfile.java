package at.frikiteysch.repong.storage;

import java.io.Serializable;

public class RePongProfile implements Serializable{
	
	private static final long serialVersionUID = -8680213464074232157L;
	
	private String name;
	private int totalDuration; //the duration in MINUTES!!
	private int playCount; //count of the games the user has played
	private int winCount; //count of the winning games of the user
	private int userId;
	
	public RePongProfile()
	{
		this.name = "";
		this.totalDuration = 0;
		this.playCount = 0;
		this.winCount = 0;
		this.userId = -1; // not logged in
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getTotalDuration() {
		return totalDuration;
	}

	public void setTotalDuration(int totalDuration) {
		this.totalDuration = totalDuration;
	}

	public int getPlayCount() {
		return playCount;
	}

	public void setPlayCount(int playCount) {
		this.playCount = playCount;
	}

	public int getWinCount() {
		return winCount;
	}

	public void setWinCount(int winCount) {
		this.winCount = winCount;
	}
	
	@Override
	public String toString()
	{
		return "RePongProfile:\nUserId <" + userId + ">, Name<" + name + ">, Total Duration<" + totalDuration + ">, "
				+ "Count Of Plays<" + playCount + ">, Count Of Wins<" + winCount + ">";
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}
}
