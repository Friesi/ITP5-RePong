package at.frikiteysch.repong;

import java.io.Serializable;

public class ComTerminate  implements Serializable {

	private static final long serialVersionUID = 6L;
	
	private int userId;

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}
}
