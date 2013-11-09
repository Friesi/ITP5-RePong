package at.frikiteysch.repong.communication;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;
import java.net.Socket;
import java.net.UnknownHostException;


public class CommunicationCenter {
	
	static public void sendComObjectToServer(Socket s, Object comObjectToSend)
	{
		try {
			//"ec2-54-200-186-85.us-west-2.compute.amazonaws.com", 3456);
        ObjectOutputStream out = new ObjectOutputStream(s.getOutputStream());
        out.writeObject(comObjectToSend);
        out.flush();
        
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
	}
	
	static public Object recieveComObjectFromServer(Socket s){
		// Answer from server
        ObjectInputStream inputObject;
		try {
			inputObject = new ObjectInputStream(s.getInputStream());
		
			return inputObject.readObject();
		
		} catch (StreamCorruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
    }
	
}
