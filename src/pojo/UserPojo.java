package pojo;

public class UserPojo {
	// 客户端用户id
	private String id;
	
	// 客户端用户名
	private String name;
	
	// 客户端ip地址
	private String ip;
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	/**
	 * 以 账户id判断是不是同一个用户
	 */
	@Override
	public boolean equals(Object obj) {
		if(!(obj instanceof UserPojo))
			return false;
		UserPojo other = (UserPojo)obj;
		if(other.getId().equals(this.id))
			return true;
		return false;
	}
	
	/**
	 * 以 账户id判断是不是同一个用户
	 */
	@Override
	public int hashCode() {
		return this.id.hashCode();
	}

	@Override
	public String toString() {
		return "UserPojo [id=" + id + ", name=" + name + ", ip=" + ip + "]";
	}
}