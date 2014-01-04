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


public class CommunicationCenter {
	
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
	
	static public Object recieveComObjectFromServer(Socket s){
		// Answer from server
        ObjectInputStream inputObject;
		try {
			long time = System.currentTimeMillis();
			inputObject = new ObjectInputStream(new BufferedInputStream(s.getInputStream()));
			LOGGER.info("COMCENTER: receive " + (System.currentTimeMillis() - time));
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
