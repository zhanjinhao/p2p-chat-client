package utils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import pojo.UserPojo;

/**
 * 
 * 用于管理用户列表的类，设置为单例
 * @author ISJINHAO
 *
 */
public class UserManager {
	
	//String 是  用户id
	private Map<String, UserPojo> onLineUsers;

	private final static UserManager INSTANCE = new UserManager();
	
	private UserManager() {
		onLineUsers = new ConcurrentHashMap<String, UserPojo>();
	}

	public static UserManager getInstance() {
		return INSTANCE;
	}
	
	
	public Map<String, UserPojo> getOnLineUsers() {
		return onLineUsers;
	}
	
	public Boolean addUser(UserPojo user) {
		if(onLineUsers.containsKey(user.getId()))
			return false;
		onLineUsers.put(user.getId(), user);
		return true;
	}
	
	/**
	 * 根据 用户id 得到用户的详细信息
	 * @param userId
	 * @return User 或者  null
	 */
	public UserPojo getUser(String userId) {
		return onLineUsers.get(userId);
	}
	
}