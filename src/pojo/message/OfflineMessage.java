package pojo.message;

import constant.MessageType;

public class OfflineMessage extends Message{
	
	private static final long serialVersionUID = 1L;
	
	private String id;
	
	private String type = MessageType.OFFLINE;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getType() {
		return type;
	}
	
}