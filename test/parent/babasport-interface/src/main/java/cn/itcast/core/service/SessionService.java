package cn.itcast.core.service;

/**
 * Session服务类接口
 * 
 * 模拟官方session功能，并将session数据存放到redis中
 * 
 * @author Administrator
 *
 */
public interface SessionService {
	
	/**
	 * 保存用户名到redis中
	 * 
	 * @param key  uuid
	 * @param value
	 */
	public void addUsernameToRedis(String key, String value);
	
	
	/**
	 * 从redis中取出用户名
	 * 
	 * @param key
	 * @return
	 */
	public String getUsernameForRedis(String key);

}
