package at.frikiteysch.repong.game;

import at.frikiteysch.repong.ComGameData;

/**
 * This interface provides methods for the gameplay
 *
 */
public interface GameDataCalculator{
	
	public void updatePaddle(int userId, int paddlePosition, int paddleWidth);
	public ComGameData getGameData();
	public void recalculate();
	public boolean gameFinished();
}
