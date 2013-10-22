package at.frikiteysch.repong;

import java.io.IOException;
import java.io.ObjectInputStream;

public class IncomingPackageSwitch extends Thread {
	
	private ObjectInputStream inputStream = null;
	
	public IncomingPackageSwitch(ObjectInputStream inputStream)
	{
		this.inputStream = inputStream;
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
			ComLogin objectReceived = (ComLogin) inputObject;
			System.out.println(objectReceived.getUserName());
		}
		else if (inputObject instanceof ComTerminate)
		{
			ComTerminate objectReceived = (ComTerminate) inputObject;
		}
		
		
	}
}