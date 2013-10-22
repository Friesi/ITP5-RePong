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
	@SuppressWarnings("resource")
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		System.out.println("Server gestartet!\n");
		
		ServerSocket server = null;
		
		try {
			server = new ServerSocket(3456);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		while(true) {
			try {
				Socket s = server.accept();
				ObjectInputStream in = new ObjectInputStream(s.getInputStream());
				Thread t = new Thread( new IncomingPackageSwitch(in, s) );	// neuen Thread mit neuem Packet öffnen
				t.start();
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
