package at.frikiteysch.repong;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class IncomingPackageSwitch extends Thread {
	
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
			try {
				ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
				out.writeObject(comLoginObject);
				out.flush();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else if (inputObject instanceof ComTerminate)
		{
			ComTerminate objectReceived = (ComTerminate) inputObject;
		}
		
		
	}
}