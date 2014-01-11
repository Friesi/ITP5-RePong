package at.frikiteysch.repong.defines;

public class RePongDefines {
	/**
	 * Enumeration for the position of the paddle
	 *
	 */
	public enum PaddleOrientation
	{
		NORTH,
		EAST,
		WEST,
		SOUTH,
		;
	}
	
	public static final int EXPIRE_TIMEOUT = 30000;
	public static final int SLEEP_DURATION_TERMINATOR = 30000;
	public static final int SLEEP_DURATION_HERBERT = 9000;
	public static final int SLEEP_DURATION_GETCOMWAITINFO = 2000;
	
	/**
	 * Enumeration for defining error code and error message
	 * Use this enum when sending back errors to the client
	 */
	public enum Error
	{
		GENERAL_ERROR(-1, "general error"),
		NOT_LOGGED_IN(0, "user not logged in"),
		NO_ANSWER_ASYNCTASK(-2, "got no answer during asynctask"),
		NO_SUCH_GAME(10, "no game with this id available"),
		;
		
		Error(int error_code, String error_msg)
		{
			this.error_code = error_code;
			this.error_msg = error_msg;
		}
		private int error_code;
		private String error_msg;
		
		public int getErrorCode()
		{
			return error_code;
		}
		
		public String getErrorMsg()
		{
			return error_msg;
		}
		
		@Override
		public String toString()
		{
			return "Error: Code<" + error_code + "> Msg<" + error_msg + ">";
		}
	}
	
	
	public static final int ERROR_CODE_NOT_LOGGED_IN = 1;
	public static final int ERROR_MSG_NOT_LOGGED_IN = 2;
	
	public static final int DEFAULT_PLAYER_HEARTS = 5;
	public static final int DEFAULT_BALL_SIZE = 10;
}
