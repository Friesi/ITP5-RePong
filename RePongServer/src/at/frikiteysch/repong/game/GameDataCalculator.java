package at.frikiteysch.repong.game;

import at.frikiteysch.repong.ComGameData;

public interface GameDataCalculator{
	
	public void updatePaddle(int userId, int paddlePosition);
	public ComGameData getGameData();
	public void recalculate();
}
