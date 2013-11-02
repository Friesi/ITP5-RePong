package at.frikiteysch.repong;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

import at.frikiteysch.repong.ComLogin;
import at.frikiteysch.repong.players.TerminatorThread;

public class ServerMain {
	private static Logger LOGGER = Logger.getLogger(ServerMain.class.getName());
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		LOGGER.log(Level.INFO, "Server started!");
		
		Thread terminator = new Thread( new TerminatorThread() );
		terminator.start();
		
		NetworkListener netListener = new NetworkListener();
		netListener.listen();
	}
}
