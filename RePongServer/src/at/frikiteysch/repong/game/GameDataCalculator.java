package at.frikiteysch.repong.game;

import at.frikiteysch.repong.ComGameData;

public interface GameDataCalculator{
	
	public void updatePaddle(int userId, int paddlePosition, int paddleWidth);
	public ComGameData getGameData();
	public void recalculate();
	public boolean gameFinished();
}
