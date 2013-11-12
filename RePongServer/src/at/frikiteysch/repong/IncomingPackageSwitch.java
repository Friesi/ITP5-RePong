package at.frikiteysch.repong;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import java.util.logging.Logger;

import at.frikiteysch.repong.players.PlayerList;
import at.frikiteysch.repong.communication.CommunicationCenter;
import at.frikiteysch.repong.game.GameManager;

public class IncomingPackageSwitch extends Thread {
	
	private static Logger LOGGER = Logger.getLogger(IncomingPackageSwitch.class.getName());
	
	private ObjectInputStream inputStream = null;
	private Socket socket = null;
	
	public IncomingPackageSwitch(ObjectInputStream inputStream, Socket socket)
	{
		LOGGER.log(Level.INFO, "Incoming Package Switch started");
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
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		/** Hier ist die Liste aller empfangbaren Com-Objekte und ihre Aktion **/
		
		if (inputObject instanceof ComLogin)
		{
			ComLogin comLoginObject = (ComLogin) inputObject;
			PlayerList.getInstance().generateIdForPlayer(comLoginObject, socket);
			
			System.out.println(comLoginObject.getUserName() + ", playerId: " + comLoginObject.getUserId());
			
			
			// Send ComLogin back to Client
			CommunicationCenter.sendComObjectToClient(socket, comLoginObject);
		}
		else if (inputObject instanceof ComTerminate)
		{
			ComTerminate objectReceived = (ComTerminate) inputObject;
			PlayerList.getInstance().removePlayer(objectReceived.getUserId());
			LOGGER.log(Level.INFO, "Player with id<" + objectReceived.getUserId() + "> removed");
		}
		else if (inputObject instanceof ComRefreshGameList){
						
			ComGameList comGameList = new ComGameList();
			comGameList.setGameListInfo(null);
			
			// Send ComGamelist back to Client
			CommunicationCenter.sendComObjectToClient(socket, comGameList);
		}
		else if (inputObject instanceof ComCreateGame) {
			
			ComWaitInfo waitInfo = GameManager.getInstance().createGame((ComCreateGame)inputObject);
			
			// Send ComWaitInfo back to Client
			CommunicationCenter.sendComObjectToClient(socket, waitInfo);
		}
	}
}