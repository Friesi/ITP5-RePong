package at.frikiteysch.repong;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import at.frikiteysch.repong.ComLogin;

public class ServerMain {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		System.out.println("Server gestartet!\n");
		
		ServerSocket server = null;
		ComLogin objectReceived = null;
		
		try {
			server = new ServerSocket(3456);
			Socket s = server.accept();
			ObjectInputStream in = new ObjectInputStream(s.getInputStream());
			objectReceived = (ComLogin) in.readObject();
		} catch (IOException | ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if (objectReceived != null) {
			System.out.println(objectReceived.getName());
		}
	}

}
