package at.frikiteysch.repong.game;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
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
	private int paddleDistanceFromWall = 50;	
	private boolean gameFinished = false; // indicates if game is finished (all player except for one have 0 lives)
	
	private static final Logger LOGGER = Logger.getLogger(GameDataCalculatorImpl.class.getName());
	
	public GameDataCalculatorImpl(List<Player> player)
	{
		
		ballSpeedX = 5;
		ballSpeedY = 8;
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
	
	/**
	 * this method updates the paddle of a specific player
	 */
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

	/**
	 * This method returns the ComGameData object
	 * according to the game.
	 */
	@Override
	public ComGameData getGameData() {
		ComGameData gameData = new ComGameData();
		gameData.setBall(ball);
		gameData.setField(field);
		gameData.setPlayerList((ArrayList<Player>) player);
		return gameData;
	}

	/**
	 * this method recalculates the game.
	 * included are all gameplay points such as ball-movement, paddle-movement, collision-detection, ....
	 * 1.) handle collisions to the paddles
	 * 2.) handle collisions to the wall
 	 * 3.) handle ordinary ball movement
	 * 4.) rearrange the ball positions
 	 * 5.) check if game has finished
 	 * 
	 * for further information read the comments inside this method
	 */
	@Override
	public void recalculate() {
		boolean moved = false;
		int ballSize = ball.getSize();
		Position position = ball.getPosition();
		Position prevPosition = prevBall.getPosition();
		Position newPosition = new Position();
		
		// 1.) handle collisions to the paddles
		// 2.) handle collisions to the wall
		// 3.) handle ordinary ball movement
		// 4.) rearrange the ball positions
		// 5.) check if game has finished
		
		// 1.)
					for (Player p : player)
					{ 
						switch(p.getOrientation())
						{
							case SOUTH:
							if(prevPosition.getY()<gameField-paddleDistanceFromWall){
								if ((position.getY() + ballSize) >= (gameField - paddleDistanceFromWall)) {	// ball on bottom height of paddle
									if ((position.getX() >= p.getPosition()) && ( (position.getX() + ballSize) <= (p.getPosition() + p.getWidth()))) {	// collision on paddle
										
										//set X speed, depending on which paddle area is hit by the ball
										if(position.getX() >= (p.getPosition()+p.getWidth()/2)){
											ballSpeedX+=1;
										}
										else{
											ballSpeedX-=1;
										}
										
										//reverse Y speed
										ballSpeedY = -ballSpeedY;
										
										
										//set moved flags
										moved = true;
									}
								}
							}
							break;
						
							case NORTH:
							if(prevPosition.getY()>paddleDistanceFromWall){
								if (position.getY() <= paddleDistanceFromWall) {	// ball on bottom height of paddle
									if ((position.getX() >= (gameField-p.getPosition()-p.getWidth())) && ( (position.getX() + ballSize) <= (gameField -p.getPosition()))) {	// collision on paddle
										
										//set X speed, depending on which paddle area is hit by the ball
									if(position.getX() <= (p.getPosition()+p.getWidth()/2)){
											ballSpeedX+=1;
										}
										else{
											ballSpeedX-=1;
										}
									
										//reverse Y speed
										ballSpeedY = -ballSpeedY;
										
										
										//set moved flags
										moved = true;
									}
								}
							}
								break;
								
							case WEST:
							if(prevPosition.getX()>paddleDistanceFromWall){
								if (position.getX() <= paddleDistanceFromWall) {	// ball on bottom height of paddle
									if ((position.getY() >= p.getPosition()) && ( (position.getY() + ballSize) <= (p.getPosition() + p.getWidth()))) {	// collision on paddle
										
										//set Y speed, depending on which paddle area is hit by the ball
										if(position.getY() >= (p.getPosition()+p.getWidth()/2)){
											ballSpeedY+=1;
										}
										else{
											ballSpeedY-=1;
										}
										
										//reverse X speed
										ballSpeedX = -ballSpeedX;
										
										
										//set moved flags
										moved = true;
									}
								}
							}
								break;
								
							case EAST:
							if(prevPosition.getX()<(gameField-paddleDistanceFromWall)){
								if (position.getX() >= (gameField-paddleDistanceFromWall)) {	// ball on bottom height of paddle
									if ((position.getY() >= (gameField-p.getPosition()-p.getWidth()) && ( (position.getY() + ballSize) <= (gameField-p.getPosition())))) {	// collision on paddle
										
										//set Y speed, depending on which paddle area is hit by the ball
										if(position.getY() <= (p.getPosition()+p.getWidth()/2)){
											ballSpeedY+=1;
										}
										else{
											ballSpeedY-=1;
										}
									
										//reverse X speed
										ballSpeedX = -ballSpeedX;
										
										
										//set moved flags
										moved = true;
									}
								}
							}
								break;
						}
					}
		
		// 2.)
		if(!moved){
			if ((position.getX() + ballSize) >= gameField) // collision on the right
			{
				ballSpeedX = -ballSpeedX;
			//	moved = true;
				reduceLife(PaddleOrientation.EAST);
			}
			else if ((position.getX() - ballSize <= 0)) // collision on the left
			{
				ballSpeedX = -ballSpeedX;
			//	moved = true;
				reduceLife(PaddleOrientation.WEST);
			}
			
			if ((position.getY() + ballSize) >= gameField) // collision on the bottom
			{
				ballSpeedY = -ballSpeedY;
			//	moved = true;
				reduceLife(PaddleOrientation.SOUTH);
			}
			else if ((position.getY() - ballSize) <= 0) // collision on the top
			{
				ballSpeedY = -ballSpeedY;
			//	moved = true;
				reduceLife(PaddleOrientation.NORTH);
			}
		}
		
		// 3.)
		// is provided under 4.
		
		// 4.)
		//set new position
		newPosition.setX(position.getX() + ballSpeedX);
		newPosition.setY(position.getY() + ballSpeedY);
		prevBall.setPosition(position);
		ball.setPosition(newPosition);
		
		// 5.)
		// check if game has finished
		if (player.size() > 1) // normal game
		{
			int playersAlive = 0;
			for (Player p : player)
			{
				if (p.getLifes() > 0)
					playersAlive++;
			}
			if (playersAlive <= 1)
				gameFinished = true;
		}
		else if (player.size() == 1) // practice mode
		{
			Player p = player.get(0);
			if (p.getLifes() == 0)
				gameFinished = true;
		}
		
	}

	/**
	 * this method reduces life of the player according to its orientation
	 * @param orientation
	 */
	private void reduceLife(PaddleOrientation orientation) {
		switch (orientation){
			case NORTH: LOGGER.info("Reduce life of player in the north!"); 
						for(Player check: player){
							if((check.getLifes()>0)&&(check.getOrientation()==PaddleOrientation.NORTH)){
								check.setLifes(check.getLifes()-1);
							}
						}
						break;
						
			case EAST:  LOGGER.info("Reduce life of player in the east!"); 
						for(Player check: player){
							if((check.getLifes()>0)&&(check.getOrientation()==PaddleOrientation.EAST)){
								check.setLifes(check.getLifes()-1);
							}
						}
						break;
			case SOUTH: LOGGER.info("Reduce life of player in the south!");
						for(Player check: player){
							if((check.getLifes()>0)&&(check.getOrientation()==PaddleOrientation.SOUTH)){
								check.setLifes(check.getLifes()-1);
							}
						}
						break;
			case WEST:  LOGGER.info("Reduce life of player in the west!"); 
						for(Player check: player){
							if((check.getLifes()>0)&&(check.getOrientation()==PaddleOrientation.WEST)){
								check.setLifes(check.getLifes()-1);
							}
						}
						break;
						
		}
	}
	
	public boolean gameFinished()
	{
		return gameFinished;
	}

}
