package at.frikiteysch.repong;

import java.io.Serializable;

public class ComPaddlePosition  implements RequiresLoggedInUserObject, Serializable {

	private static final long serialVersionUID = 9L;
	
	private int positionNorm;
	private int userId;

	public int getPositionNorm() {
		return positionNorm;
	}

	public void setPositionNorm(int positionNorm) {
		this.positionNorm = positionNorm;
	}

	@Override
	public int getUserId() {
		return userId;
	}

	@Override
	public void setUserId(int userId) {
		this.userId = userId;
	}
}
