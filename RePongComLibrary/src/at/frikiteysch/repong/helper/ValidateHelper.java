package at.frikiteysch.repong.helper;

public class ValidateHelper {
	
	/**
	 * Validates the given String as a userName.
	 * Can't be null or empty and must have less than 30 characters
	 * @param gameName
	 * @return true if valid, otherwise false
	 */
	public static boolean isValidUserName(String userName)
	{
		if (userName == null || userName.equals("") || userName.trim().equals("")) //standard
    		return false;
    	
    	if (userName.length() > 30) // length
    		return false;
    	
    	return true;
	}
	
	/**
	 * Validates the given String as a gameName.
	 * Can't be null or empty and must have less than 30 characters
	 * @param gameName
	 * @return true if valid, otherwise false
	 */
	public static boolean isValidGameName(String gameName)
	{
		if (gameName == null || gameName.equals("") || gameName.trim().equals("")) //standard
    		return false;
    	
    	if (gameName.length() > 30) // length
    		return false;
    	
    	return true;
	}
}
