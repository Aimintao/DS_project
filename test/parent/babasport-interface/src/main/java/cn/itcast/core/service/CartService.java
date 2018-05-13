package cn.itcast.core.service;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

import cn.itcast.core.pojo.Cart;

public interface CartService {

	/**
	 * 添加购物车到redis中
	 * @param username 用户名
	 * @param cart 购物车对象
	 * @throws JsonProcessingException 
	 */
	public void addCartToRedis(String username,Cart cart) throws JsonProcessingException;
	
	/**
	 * 从redis中取出购物车
	 * @param username
	 * @return
	 * @throws IOException 
	 * @throws JsonMappingException 
	 * @throws JsonParseException 
	 */
	public Cart getCartFormRedis(String username) throws JsonParseException, JsonMappingException, IOException;

	/**
	 * 填充购物项的复合信息到cart中
	 * 
	 * @param cart
	 *            item skuid、amount
	 * @return cart item skuid、amount 、 superpojo（sku）
	 */
	public Cart fillItemsSkus(Cart cart);
	
}
