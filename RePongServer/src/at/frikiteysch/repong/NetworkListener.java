package at.frikiteysch.repong;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class NetworkListener {
	
	private Logger LOGGER = Logger.getLogger(NetworkListener.class.getName());
	
	public NetworkListener()
	{
		LOGGER.log(Level.INFO, "NetworkListener started");
	}

	@SuppressWarnings("resource")
	public void listen()
	{
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
