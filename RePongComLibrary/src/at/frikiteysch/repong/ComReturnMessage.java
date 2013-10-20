package at.frikiteysch.repong;

import java.io.Serializable;

public class ComReturnMessage implements Serializable {

	private static final long serialVersionUID = 15L;
	
	private int messageId;

	public int getMessageId() {
		return messageId;
	}

	public void setMessageId(int messageId) {
		this.messageId = messageId;
	}
}
