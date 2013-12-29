package at.frikiteysch.repong.game;

import java.awt.Color;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import at.frikiteysch.repong.Ball;
import at.frikiteysch.repong.ComGameData;
import at.frikiteysch.repong.Player;
import at.frikiteysch.repong.defines.Field;
import at.frikiteysch.repong.defines.Position;
import at.frikiteysch.repong.defines.RePongDefines;

public class GameDataCalculatorImpl implements GameDataCalculator{

	private Ball ball;
	private Ball prevBall;
	private int ballSpeedX;
	private int ballSpeedY;
	private List<Player> player;
	
	private Field field;
	private int gameField = 1000; // squared gamefield, so only one side is needed
	
	private static final Logger LOGGER = Logger.getLogger(GameDataCalculatorImpl.class.getName());
	
	public GameDataCalculatorImpl(List<Player> player)
	{
		ballSpeedX = 10;
		ballSpeedY = 10;
		ball = new Ball();
		ball.setPosition(new Position(gameField/2, gameField/2));
		ball.setSize(RePongDefines.DEFAULT_BALL_SIZE);
		ball.setColor(Color.WHITE.getRGB());
		
		prevBall = new Ball();
		prevBall.setPosition(new Position((gameField/2) -ballSpeedX, (gameField/2)));
		prevBall.setSize(RePongDefines.DEFAULT_BALL_SIZE);
		prevBall.setColor(Color.WHITE.getRGB());
		
		field = new Field();
		field.setHeight((double) gameField);
		field.setWidth((double) gameField);
		this.player = player;
		for (Player p : player)
			p.setPosition(gameField/2);
	}
	
	@Override
	public void updatePaddle(int userId, int paddlePosition) {
		// TODO Auto-generated method stub
		for (Player p : player)
		{
			if (p.getUserId() == userId)
			{
				p.setPosition(paddlePosition);
			}
		}
	}

	@Override
	public ComGameData getGameData() {
		ComGameData gameData = new ComGameData();
		gameData.setBall(ball);
		gameData.setField(field);
		gameData.setPlayerList((ArrayList) player);
		return gameData;
	}

	@Override
	public void recalculate() {
		// TODO calculate ball position and check if ball position hits paddle or wall
		int ballSize = ball.getSize();
		Position position = ball.getPosition();
		Position prevPosition = prevBall.getPosition();
		Position newPosition = new Position();
		
		// 1.) handle collisions to the wall
		// 2.) handle collisions to the paddles
		// 3.) handle ordinary ball movement
		// 4.) rearrange the ball positions
		
		// 1.)
		if ((position.getX() + ballSize) == gameField) // collision on the right
		{
			newPosition.setX(position.getX() - ballSpeedX);
		}
		else if ((position.getX() - ballSize == 0)) // collision on the left
		{
			newPosition.setX(position.getX() + ballSpeedX);
		}
		
		if ((position.getY() + ballSize) == gameField) // collision on the bottom
		{
			newPosition.setY(position.getY() - ballSpeedY);
		}
		else if ((position.getY() - ballSize) == 0) // collision on the top
		{
			newPosition.setY(position.getY() + ballSpeedY);
		}
		
		// 4.)
		prevBall.setPosition(position);
		ball.setPosition(newPosition);
		
		LOGGER.info("recalculated ball position" + "position: <" + newPosition.getX() + "/" + newPosition.getY() + ">");
	}

}
