package cn.itcast.core.service;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import cn.itcast.core.pojo.Cart;
import cn.itcast.core.pojo.Item;
import cn.itcast.core.pojo.SuperPojo;
import redis.clients.jedis.Jedis;

@Service("cartService")
public class CartServiceImpl implements CartService{

	@Autowired
	private Jedis jedis;
	
	@Autowired
	private SkuService skuService;
	
	@Override
	public void addCartToRedis(String username, Cart cart) throws JsonProcessingException {
		//将cart对象转成json
		ObjectMapper om = new ObjectMapper();
		om.setSerializationInclusion(Include.NON_NULL);
		String cartJson = om.writeValueAsString(cart);
		
		//将购物车的json字符串存入到redis中
		jedis.set("cart:"+username, cartJson);
	}

	@Override
	public Cart getCartFormRedis(String username) throws JsonParseException, JsonMappingException, IOException {
		String cartJson = jedis.get("cart:"+username);
		if(cartJson==null)
		{
			return null;
		}
		ObjectMapper om = new ObjectMapper();
		Cart cart = om.readValue(cartJson, Cart.class);
		
		return cart;
	}
	

	@Override
	public Cart fillItemsSkus(Cart cart) {
		List<Item> items = cart.getItems();

		for (Item item : items) {
			Long skuId = item.getSkuId();

			// 根据库存id查询复合型库存对象（颜色名称、商品名称）
			SuperPojo sku = skuService.findSkuAndColorAndProductBySkuId(skuId);
			item.setSku(sku);
		}
		return cart;
	}

}
