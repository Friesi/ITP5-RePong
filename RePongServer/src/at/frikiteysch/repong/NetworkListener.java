package at.frikiteysch.repong;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class provides a listener for the network.
 * It handles socket-requests and forwards them to be handled in a separate thread.
 *
 */
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
				Thread t = new Thread( new IncomingPackageSwitch(in, s) );	// start new thread with new package
				t.start();
				
			} catch (IOException e) {
				LOGGER.log(Level.SEVERE, null, e);
			}
		}
	}
}
