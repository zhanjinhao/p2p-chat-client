package constant;

import java.util.ResourceBundle;

public class AgreementsConfig {
	
	// 约定 群发时目的用户的id
	public static String PUBLIC_MESSGE;
	
	// 约定 文件发送请求的字段
	public static String FILE_REQ;
	
	// 约定 文件接收响应的字段
	public static String FILE_RES;
	
	static {
		
		ResourceBundle bundle = ResourceBundle.getBundle("base");
		PUBLIC_MESSGE = bundle.getString("public_messge");
		FILE_REQ = "1";
		FILE_RES = "2";
	}
}