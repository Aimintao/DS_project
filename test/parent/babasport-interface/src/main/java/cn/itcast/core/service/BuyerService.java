package cn.itcast.core.service;

import cn.itcast.core.pojo.Buyer;

/**
 * 购买者服务接口
 * @author Administrator
 *
 */
public interface BuyerService {

	/**
	 * 根据用户名 找到用户对象
	 * @param username
	 * @return
	 */
	public Buyer findByUsername(String username);
	
}
