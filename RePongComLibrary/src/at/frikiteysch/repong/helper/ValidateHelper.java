package at.frikiteysch.repong.helper;

public class ValidateHelper {
	public static boolean isValidUserName(String userName)
	{
		if (userName == null || userName.equals("")) //standard
    		return false;
    	
    	if (userName.length() > 30) // length
    		return false;
    	
    	return true;
	}
}
