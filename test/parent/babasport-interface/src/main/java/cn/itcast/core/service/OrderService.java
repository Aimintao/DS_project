package cn.itcast.core.service;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

import cn.itcast.core.pojo.Order;

/**
 * 订单服务接口
 * @author Administrator
 *
 */
public interface OrderService {
	
	/**
	 * 保存订单及订单详情
	 * 
	 * @param order
	 * @param username
	 * @throws IOException 
	 * @throws JsonMappingException 
	 * @throws JsonParseException 
	 */
	public void addOrderAndDetail(Order order,String username) throws JsonParseException, JsonMappingException, IOException;

}
