package at.frikiteysch.repong;

import java.util.logging.Level;
import java.util.logging.Logger;

import at.frikiteysch.repong.players.TerminatorThread;

/**
 * This class starts the terminator thread and the tells the network listener
 * to listen for events.
 */
public class ServerMain {
	private static Logger LOGGER = Logger.getLogger(ServerMain.class.getName());

	/**
	 * Main-Method
	 * Starting point
	 */
	public static void main(String[] args) {
		LOGGER.log(Level.INFO, "Server started!");
		
		Thread terminator = new Thread( new TerminatorThread() );
		terminator.start();
		
		NetworkListener netListener = new NetworkListener();
		netListener.listen();
	}
}
