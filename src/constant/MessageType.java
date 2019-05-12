package constant;

/**
 * 
 * 用于由json字符串生成对应的对象时确定对象时Message的哪个子类
 * @author ISJINHAO
 *
 */
public class MessageType {
	
	public static String ROOT = "00";
	public static String CHAT = "01";
	public static String FILE = "02";
	public static String REGISTER = "03";
	public static String CLIENT_STATE = "04";
	public static String LOGIN = "05";
	public static String OFFLINE = "06";
	
}
