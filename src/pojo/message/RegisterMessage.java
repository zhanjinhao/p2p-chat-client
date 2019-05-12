package pojo.message;

import constant.MessageType;

/**
 * 
 * 注册的时候传输 账号和密码 ，账号是邮箱号
 * @author ISJINHAO
 *
 */
public class RegisterMessage extends Message{
	private static final long serialVersionUID = 1L;
	private String id;
	private String pwd;
	
	private String type = MessageType.REGISTER;
	
	public String getType() {
		return type;
	}
	
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getPwd() {
		return pwd;
	}
	public void setPwd(String pwd) {
		this.pwd = pwd;
	}
	
}