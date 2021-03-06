package at.frikiteysch.repong;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

import at.frikiteysch.repong.communication.CommunicationCenter;
import at.frikiteysch.repong.defines.RePongDefines.Error;
import at.frikiteysch.repong.game.GameManager;
import at.frikiteysch.repong.herbert.HerbertHandler;
import at.frikiteysch.repong.players.PlayerList;

/**
 * This class can handle incoming packages
 *
 */
public class IncomingPackageSwitch extends Thread {
	
	private static Logger LOGGER = Logger.getLogger(IncomingPackageSwitch.class.getName());
	
	private ObjectInputStream inputStream = null;
	private Socket socket = null;
	
	public IncomingPackageSwitch(ObjectInputStream inputStream, Socket socket)
	{
		this.inputStream = inputStream;
		this.socket = socket;
	}
	
	@Override 
	public void run()
	{
		Object inputObject = null;
		
		try {
			inputObject = inputStream.readObject();
		} catch (ClassNotFoundException e) {
			LOGGER.log(Level.SEVERE, null, e);
		} catch (IOException e) {
			LOGGER.log(Level.SEVERE, null, e);
		}
		
		
		/** Hier ist die Liste aller empfangbaren Com-Objekte und ihre Aktion **/
		if (inputObject instanceof ComLogin)
		{
			ComLogin comLoginObject = (ComLogin) inputObject;
			PlayerList.getInstance().generateIdForPlayer(comLoginObject, socket);
			
			LOGGER.info(comLoginObject.getUserName() + ", playerId: " + comLoginObject.getUserId());
			
			
			// Send ComLogin back to Client
			CommunicationCenter.sendComObjectToClient(socket, comLoginObject);
		}
		else // user should be logged in - if not send back error not-logged in
		{
			if (inputObject instanceof RequiresLoggedInUserObject)
			{
				RequiresLoggedInUserObject object = (RequiresLoggedInUserObject) inputObject;
				int userId = object.getUserId();
				
				// there is no user logged in so send error message back
				if (PlayerList.getInstance().getPlayerList().get(userId) == null)
				{
					ComError error = new ComError();
					error.setErrorCode(Error.NOT_LOGGED_IN.getErrorCode());
					error.setError(Error.NOT_LOGGED_IN.getErrorMsg());
					CommunicationCenter.sendComObjectToClient(socket, error);
					
					LOGGER.severe("user with id<" + userId + "> is not logged in, error returned");
					return; // stop work
				}
			}
		}
		
		if (inputObject instanceof Herbert)
		{
			HerbertHandler herbertHandler = new HerbertHandler((Herbert)inputObject);
			herbertHandler.updateTimestamp();
		}
		else if (inputObject instanceof ComTerminate)
		{
			ComTerminate objectReceived = (ComTerminate) inputObject;
			PlayerList.getInstance().removePlayer(objectReceived.getUserId());
			LOGGER.log(Level.INFO, "Player with id<" + objectReceived.getUserId() + "> removed");
		}
		else if (inputObject instanceof ComRefreshGameList){
			LOGGER.log(Level.INFO,"Obtain and return GameList");		
			ComGameList comGameList = new ComGameList();
			comGameList.setGameListInfo(GameManager.getInstance().getGameListInfo());
			LOGGER.log(Level.INFO,"ComGameList: "+comGameList.getGameListInfo().getClass().getCanonicalName());
			LOGGER.log(Level.INFO,"ComGameList size: "+comGameList.getGameListInfo().size());
			// Send ComGamelist back to Client
			CommunicationCenter.sendComObjectToClient(socket, comGameList);
		}
		else if (inputObject instanceof ComCreateGame) 
		{
			ComCreateGame createGameObject = (ComCreateGame)inputObject;
			GameManager.getInstance().createGame(createGameObject, socket);
		}
		else if (inputObject instanceof ComWaitInfo)
		{
			GameManager.getInstance().getComWaitInfo(((ComWaitInfo) inputObject).getGameId(), socket);
		}
		else if (inputObject instanceof ComJoinGame)
		{
			GameManager.getInstance().joinGame((ComJoinGame) inputObject, socket);
		}
		else if (inputObject instanceof ComLeaveGame)
		{
			ComLeaveGame objectReceived = (ComLeaveGame) inputObject;
			GameManager.getInstance().leaveGame(objectReceived);
		}
		else if (inputObject instanceof ComStartGame)
		{
			ComStartGame startGameObject = (ComStartGame) inputObject;
			GameManager.getInstance().startGame(startGameObject.getGameId(), socket);
		}
		else if (inputObject instanceof ComPaddlePosition)
		{
			ComPaddlePosition position = (ComPaddlePosition) inputObject;
			GameManager.getInstance().handlePaddlePosition(position.getGameId(), position.getUserId(), position.getPositionNorm(), position.getWidthNorm(), socket);
			
		}
		
		try {
			socket.close();
		} catch (IOException e) {
			LOGGER.log(Level.SEVERE, null, e);
		}
	}
}