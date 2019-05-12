package pojo.message;

import com.alibaba.fastjson.JSONObject;

import constant.MessageType;

public class ChatMessage extends Message{

	private static final long serialVersionUID = 1L;
	
	private String type = MessageType.CHAT;
	
	public String getType() {
		return type;
	}

	private String content;

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	@Override
	public String toString() {
		return "ChatMessage [content=" + content + "]";
	}
	
	public static void main(String[] args) {
		
		ChatMessage cm = new ChatMessage();
		System.out.println(JSONObject.toJSONString(cm));
		
	}
	
}
