package at.frikiteysch.repong;

import java.io.Serializable;

public class ComPaddlePosition  implements Serializable {

	private static final long serialVersionUID = 9L;
	
	private int positionNorm;

	public int getPositionNorm() {
		return positionNorm;
	}

	public void setPositionNorm(int positionNorm) {
		this.positionNorm = positionNorm;
	}
}
