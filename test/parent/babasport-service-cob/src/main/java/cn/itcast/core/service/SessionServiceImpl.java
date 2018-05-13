package cn.itcast.core.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import redis.clients.jedis.Jedis;

@Service("sessionService")
public class SessionServiceImpl implements SessionService {

	@Autowired
	private Jedis jedis;

	@Override
	public void addUsernameToRedis(String key, String value) {
		// key是uuid（maosessionid） value是用户名
		// 因为考虑到以后可能还有验证码，所以在key后面加个username再保存到redis中
		jedis.set(key + ":username", value);
		// 设置失效时间 单位秒
		jedis.expire(key + ":username", 1800);

	}

	@Override
	public String getUsernameForRedis(String key) {

		String value = jedis.get(key + ":username");

		if(value!=null)
		{
			// 失效时间应该从最后一次访问开始算起，所以让value有值时，则延长续命时间
			// 设置失效时间 单位秒
			jedis.expire(key + ":username", 1800);
		}
		return value;
	}

}
