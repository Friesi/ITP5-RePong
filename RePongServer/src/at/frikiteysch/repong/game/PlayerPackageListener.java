package at.frikiteysch.repong.game;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

import at.frikiteysch.repong.ComLeaveGame;
import at.frikiteysch.repong.Herbert;
import at.frikiteysch.repong.IncomingPackageSwitch;

public class PlayerPackageListener extends Thread {

	private static Logger LOGGER = Logger.getLogger(PlayerPackageListener.class.getName());
	private Socket socket = null;
	
	public PlayerPackageListener(Socket socket)
	{
		this.socket = socket;
	}
	
	@Override 
	public void run()
	{
		Object inputObject = null;
		
		try {
			ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());
			LOGGER.log(Level.FINE, "Incoming Player Package started");
			inputObject = inputStream.readObject();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if (inputObject instanceof ComLeaveGame)
		{
			LOGGER.info("juhu ende");
		}
	}
}
