package at.frikiteysch.repong.communication;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * This class provides methods to send java-objects over sockets
 *
 */
public class CommunicationCenter {
	
	/**
	 * Sends the given object over the given socket via bufferedoutputstream
	 * @param s the socket
	 * @param comObjectToSend the object
	 */
	public static void sendComObjectToClient(Socket s, Object comObjectToSend)
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
