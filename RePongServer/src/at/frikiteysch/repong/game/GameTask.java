package at.frikiteysch.repong.game;

import java.util.TimerTask;

public class GameTask extends TimerTask {
	private GameDataCalculator gamePlay;

	GameTask (GameDataCalculator gamePlay) {
		this.gamePlay = gamePlay;
	}
	
	@Override
	public void run() {
		gamePlay.recalculate();
	}

}
