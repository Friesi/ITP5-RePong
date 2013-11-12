package at.frikiteysch.repong.helper;

public class ValidateHelper {
	public static boolean isValidUserName(String userName)
	{
		if (userName == null || userName.equals("") || userName.trim().equals("")) //standard
    		return false;
    	
    	if (userName.length() > 30) // length
    		return false;
    	
    	return true;
	}
	
	public static boolean isValidGameName(String gameName)
	{
		if (gameName == null || gameName.equals("") || gameName.trim().equals("")) //standard
    		return false;
    	
    	if (gameName.length() > 10) // length
    		return false;
    	
    	return true;
	}
}
