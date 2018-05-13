package cn.itcast;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import redis.clients.jedis.Jedis;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:applicationContext.xml" })
public class TestRedis {

	/**
	 * 测试使用java代码（jedis）操作 redis服务器
	 */
	@Test
	public void testRedis() {
		
//		// 创建redis客户端对象并指定服务器地址 端口默认为6379
//		Jedis jedis = new Jedis("192.168.56.101", 6379);
//		
//		// 使redis中的pno key值加1
//		Long incr = jedis.incr("pno");
//		System.out.println(incr);

	}
	
	@Autowired
	private Jedis jedis;
	
	/**
	 * 测试使用java代码（jedis）操作 redis服务器 (Spring)
	 */
	@Test
	public void testRedis2() {
		Long incr = jedis.incr("pno");
		System.out.println(incr);
	}
	
}
