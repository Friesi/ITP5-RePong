package at.frikiteysch.repong.storage;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Map;
import java.util.logging.Logger;

import android.content.Context;

public class ProfileManager {
	
	private final static Logger LOGGER = Logger.getLogger(ProfileManager.class.getName());
	private final static String STORE_FILE_NAME = "RePong_Profile";
	
	private static ProfileManager instance = new ProfileManager();
	
	private RePongProfile profile;
	
	protected ProfileManager()
	{
		LOGGER.info("ProfilManager instance created!");
		profile = new RePongProfile();
	}
	
	public static ProfileManager getInstance()
	{
		return instance;
	}
	
	/**
	 * Loads the Profile-Data from the internal storage of the app.
	 * This method should be called right from the start of the app.
	 * NOTE: All changes to the Profile before this method was called will be gone.
	 * @param context the context of the app
	 * @return
	 */
	public boolean loadProfileFromStorage(Context context)
	{
		LOGGER.fine("loading profile from storage ...");
		ObjectInputStream ois = null;
		try{
			FileInputStream fis = context.openFileInput(STORE_FILE_NAME);
			ois = new ObjectInputStream(fis);
			this.profile = (RePongProfile) ois.readObject();;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		} catch (ClassNotFoundException e) {
			LOGGER.fine("File does not exist yet");
			return false;
		} finally {
			try {
				if (ois != null)
					ois.close();
			} catch (IOException e) {
				e.printStackTrace();
				return false;
			}
		}
		LOGGER.fine("successfully loaded profile from storage");
		LOGGER.info("data of loaded profile: " + this.profile.toString());
		return true;
	}
	
	/**
	 * Writes the Profile-Data into the internal storage of the app.
	 * This method should be called just before the programm exits.
	 * @param context the Context of the app
	 * @return
	 */
	public boolean storeProfile(Context context)
	{
		LOGGER.fine("storing profile to storage ...");
		System.out.println("Store feeds");
		ObjectOutputStream oos = null;
		try {
			FileOutputStream fos = context.openFileOutput(STORE_FILE_NAME, Context.MODE_PRIVATE);
			oos = new ObjectOutputStream(fos);
			oos.writeObject(profile);
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		} finally{
			try {
				if (oos != null)
					oos.close();
			} catch (IOException e) {
				e.printStackTrace();
				return false;
			}
		}
		LOGGER.fine("successfully stored profile!");
		LOGGER.info("data of loaded profile: " + this.profile.toString());
		return true;
	}
}
