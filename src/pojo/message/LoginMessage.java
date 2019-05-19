package pojo.message;

import java.io.File;

import constant.MessageType;

/**
 * 
 * 注册的时候传输 账号和密码 ，账号是邮箱号
 * @author ISJINHAO
 *
 */
public class LoginMessage extends Message{
	private static final long serialVersionUID = 1L;
	private String id;
	private String pwd;
	
	private String type = MessageType.LOGIN;
	
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
	@Override
	public String toString() {
		return "LoginMessage [id=" + id + ", pwd=" + pwd + ", type=" + type + "]";
	}
	
	public static void main(String[] args) {
		
		String name = new File("C:\\Users\\ISJINHAO\\Desktop\\160341237实验报告.pdf").getName();
		System.out.println(name);
		
	}
	
}