package at.frikiteysch.repong.communication;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import android.content.Context;
import android.content.res.AssetManager;

/**
 * This class provides methods to send and receive objects to/from the server.
 * Therefore it uses sockets to communicate.
 * The information for the server (address and port) may be obtained by an properties-file
 * if such file exists in the assets.
 * Otherwise it will use the localhost from the emulator (to the hosting-pc).
 *
 */
public class CommunicationCenter {
	
	/**
	 * Tries to load serverAddress and serverPort from the properties file.
	 * If such properties-file does not exist it will use ordinary values.
	 * @param context
	 */
	public static void loadProperties(Context context)
	{
		try{
			Properties p = new Properties();
			
			AssetManager assetManager = context.getAssets();
			InputStream is = assetManager.open("repong.properties");
			p.load(is);
			
			serverAddress = p.getProperty("server-address", serverAddress);
			serverPort = Integer.valueOf(p.getProperty("server-port", String.valueOf(serverPort)));
			LOGGER.info("loaded properties serverAddress<" + serverAddress + ">, serverPort<" + serverPort + "> successfully");
		}
		catch (Exception e)
		{
			LOGGER.log(Level.INFO, "could not load properties (maybe does not exist), will use standard address and port", e);
		}
	}
	
	public static String serverAddress = "10.0.2.2"; // ec2-54-200-186-85.us-west-2.compute.amazonaws.com
	public static int serverPort = 3456; 
	
	private static final Logger LOGGER = Logger.getLogger(CommunicationCenter.class.getName());
	
	/**
	 * Sends the given object via the given socket to the other party.
	 */
	static public void sendComObjectToServer(Socket s, Object comObjectToSend)
	{
		try {
	        ObjectOutputStream out = new ObjectOutputStream(new BufferedOutputStream(s.getOutputStream()));
	        out.writeObject(comObjectToSend);
	        out.flush();
        
		} catch (UnknownHostException e) {
			LOGGER.log(Level.SEVERE, "unknown host", e);
		} catch (IOException e) {
			LOGGER.log(Level.SEVERE, "basic io-exception", e);
		}
	}
	
	/**
	 * Receives an object from the other party via the given socket
	 */
	static public Object recieveComObjectFromServer(Socket s){
		// Answer from server
        ObjectInputStream inputObject;
		try {
			inputObject = new ObjectInputStream(new BufferedInputStream(s.getInputStream()));
			return inputObject.readObject();
		
		} catch (StreamCorruptedException e) {
			LOGGER.log(Level.SEVERE, "stream currupted exception", e);
		} catch (IOException e) {
			LOGGER.log(Level.SEVERE, "basic io-exception", e);
		} catch (ClassNotFoundException e) {
			LOGGER.log(Level.SEVERE, "class not found exception", e);
		}
		
		return null;
    }
	
}
