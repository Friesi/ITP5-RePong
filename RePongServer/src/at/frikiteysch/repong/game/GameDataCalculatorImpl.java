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
import at.frikiteysch.repong.defines.RePongDefines.PaddleOrientation;

public class GameDataCalculatorImpl implements GameDataCalculator{

	private Ball ball;
	private Ball prevBall;
	private int ballSpeedX;
	private int ballSpeedY;
	private List<Player> player;
	
	private Field field;
	private int gameField = 1000; // squared gamefield, so only one side is needed
	private int paddleDistanceFromWall = 100;	// TODO: bestimmen wie viel das ist... ^^
	
	private static final Logger LOGGER = Logger.getLogger(GameDataCalculatorImpl.class.getName());
	
	public GameDataCalculatorImpl(List<Player> player)
	{
		
		ballSpeedX = 3;
		ballSpeedY = 5;
		ball = new Ball();
		ball.setPosition(new Position(150,100));
		ball.setSize(RePongDefines.DEFAULT_BALL_SIZE);
		ball.setColor(Color.WHITE.getRGB());
		
		prevBall = new Ball();
		prevBall.setPosition(new Position(50, 0));
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
	public void updatePaddle(int userId, int paddlePosition, int paddleWidth) {
		for (Player p : player)
		{
			if (p.getUserId() == userId)
			{
				p.setPosition(paddlePosition);
				p.setWidth(paddleWidth);
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
		boolean movedx = false;
		boolean movedy = false;
		int ballSize = ball.getSize();
		Position position = ball.getPosition();
		Position prevPosition = prevBall.getPosition();
		Position newPosition = new Position();
		
		// 1.) handle collisions to the wall
		// 2.) handle collisions to the paddles
		// 3.) handle ordinary ball movement
		// 4.) rearrange the ball positions
		
		// 1.)
		if ((position.getX() + ballSize) >= gameField) // collision on the right
		{
			newPosition.setX(position.getX() - ballSpeedX);
			movedx = true;
		}
		else if ((position.getX() - ballSize <= 0)) // collision on the left
		{
			newPosition.setX(position.getX() + ballSpeedX);
			movedx = true;
		}
		
		if ((position.getY() + ballSize) >= gameField) // collision on the bottom
		{
			newPosition.setY(position.getY() - ballSpeedY);
			movedy = true;
		}
		else if ((position.getY() - ballSize) <= 0) // collision on the top
		{
			newPosition.setY(position.getY() + ballSpeedY);
			movedy = true;
		}
		
		// 2.)
		if ((!movedx) && (!movedy)){
			for (Player p : player)
			{ 
				switch(p.getOrientation())
				{
					case SOUTH:
						if ((position.getY() + ballSize) >= (gameField - paddleDistanceFromWall)) {	// ball on bottom height of paddle
							if ((position.getX() >= (p.getPosition()-p.getWidth()/2)) && ( (position.getX() + ballSize) <= (p.getPosition() + p.getWidth()/2))) {	// collision on paddle
								
								//set X speed, depending on which paddle area is hit by the ball
								if(position.getX() >= (p.getPosition()+p.getWidth())){
									ballSpeedX+=2;
								}
								else{
									ballSpeedX-=2;
								}
								
								//reverse Y speed
								//set new position
								newPosition.setX(position.getX() + ballSpeedX);
								newPosition.setY(position.getY() - ballSpeedY);
								
								
								//set moved flags
								movedx = true;
								movedy = true;
							}
						}
						break;
				
					case NORTH:
						break;
						
					case WEST:
						break;
						
					case EAST:
						break;
				}
			}
		}
		
		// 3.)
		if (!movedx)
		{
			if (position.getX() > prevPosition.getX())
			{
				newPosition.setX(position.getX() + ballSpeedX);
			}
			else
			{
				newPosition.setX(position.getX() - ballSpeedX);
			}
		}
		
		if(!movedy){
			
			if (position.getY() > prevPosition.getY())
			{
				newPosition.setY(position.getY() + ballSpeedY);
			}
			else
			{
				newPosition.setY(position.getY() - ballSpeedY);
			}
		}
		
		// 4.)
		prevBall.setPosition(position);
		ball.setPosition(newPosition);
		
		//LOGGER.info("recalculated ball position" + "position: <" + newPosition.getX() + "/" + newPosition.getY() + ">");
	}


}
