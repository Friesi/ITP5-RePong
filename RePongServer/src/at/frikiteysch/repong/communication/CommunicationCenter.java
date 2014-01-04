package at.frikiteysch.repong.communication;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;


public class CommunicationCenter {
	
	static public void sendComObjectToClient(Socket s, Object comObjectToSend)
	{
		try {
	        ObjectOutputStream out = new ObjectOutputStream(new BufferedOutputStream(s.getOutputStream()));
	        out.writeObject(comObjectToSend);
	        out.flush();
        
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}	
}
