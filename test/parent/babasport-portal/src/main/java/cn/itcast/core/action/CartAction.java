package cn.itcast.core.action;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import cn.itcast.core.pojo.Cart;
import cn.itcast.core.pojo.Item;
import cn.itcast.core.pojo.SuperPojo;
import cn.itcast.core.service.CartService;
import cn.itcast.core.service.SessionService;
import cn.itcast.core.service.SkuService;
import cn.itcast.core.tools.SessionTool;

/**
 * 购物车管理控制器
 * 
 * @author Administrator
 *
 */
@Controller
public class CartAction {

	@Autowired
	private SkuService skuService;

	@Autowired
	private SessionService sessionService;

	@Autowired
	private CartService cartService;

	// 显示购物车
	@RequestMapping(value = "/cart")
	public String showCart(Model model, HttpServletRequest request,
			HttpServletResponse response)
			throws JsonParseException, JsonMappingException, IOException {
		System.out.println("显示购物车");

		Cart cart1 = null;// cookie里面的购物车
		Cart cart2 = null;// redis里面的购物车
		Cart cart = null;// 最终的购物车

		// 不管用户有没有登录，从cookie中取出购物车 cart1
		cart1 = this.getCartFormCookies(request);
		System.out.println(cart1);

		// 从redis中取出用户
		String username = sessionService.getUsernameForRedis(
				SessionTool.getSessionID(request, response));

		// 用户已登录
		if (username != null) {
			// 从redis中取出购物车 cart2
			cart2 = cartService.getCartFormRedis(username);
		}

		// 合并cart
		cart = this.mergeCart(cart1, cart2);

		// 用户已登录
		if (username != null) {
			// 删除cookie中的购物车
			this.delCartFormCookies(request, response);

			// 将合并后的购物车重写入redis中
			cartService.addCartToRedis(username, cart);
		}

		if (cart != null) {
			// 开始填充cart的数据 将商品名称、颜色名称、尺码等信息放入购物车
			cart = cartService.fillItemsSkus(cart);
		}

		//System.out.println("商品数量"+cart.getProductAmount());
		
		model.addAttribute("cart", cart);

		return "cart";
	}

	// 添加购物车
	@RequestMapping(value = "/addCart")
	public String addCart(Long skuId, Integer amount,
			HttpServletRequest request, HttpServletResponse response)
			throws JsonParseException, JsonMappingException, IOException {
		System.out.println("skuId:" + skuId);
		System.out.println("amount:" + amount);
		Cart cart = null;// 总购物车

		// 将cookie中的购物车取出来  
		cart = this.getCartFormCookies(request);

		String username = sessionService.getUsernameForRedis(
				SessionTool.getSessionID(request, response));
		
		//用户已经登录
		if(username!=null)
		{
			cart = cartService.getCartFormRedis(username);
		}
	
		//判断cart如果是null的话，就新建一个
		if(cart==null)
		{
			cart = new Cart();
		}
		
		//添加item到购物车中
		Item item = new Item();
		item.setAmount(amount);
		item.setSkuId(skuId);
		cart.addItem(item);//最新的购物车
		
		//用户未登录
		if(username==null)
		{
			//将新购物车重写到cookie中
			this.addCartToCookies(response, cart);
		}
		//用户已登录
		else
		{
			//将新购物车重写到redis中
			cartService.addCartToRedis(username, cart);
		}
		return "redirect:/cart";
	}

	/**
	 * 从cookie中取出购物车对象cart
	 * 
	 * @param request
	 * @return
	 * @throws IOException
	 * @throws JsonMappingException
	 * @throws JsonParseException
	 */
	public Cart getCartFormCookies(HttpServletRequest request)
			throws JsonParseException, JsonMappingException, IOException {

		// 取得客户端的cookie
		Cookie[] cookies = request.getCookies();
		if (cookies != null && cookies.length > 0) {
			// 遍历cookie
			for (Cookie cookie : cookies) {

				if (cookie.getName().equals("cart")) {
					// 取出cookie中cart值，进行json转换
					String value = cookie.getValue();
					ObjectMapper om = new ObjectMapper();
					Cart cart = om.readValue(value, Cart.class);
					return cart;
				}
			}
		}
		return null;
	}

	/**
	 * 将购物车添加到cookie中
	 * 
	 * @param response
	 * @param cart
	 * @throws JsonProcessingException
	 */
	public void addCartToCookies(HttpServletResponse response, Cart cart)
			throws JsonProcessingException {

		// 将cart对象转成json字符串
		ObjectMapper om = new ObjectMapper();
		om.setSerializationInclusion(Include.NON_NULL);
		String cartJson = om.writeValueAsString(cart);
		System.out.println("cartJson:" + cartJson);

		// 将json字符串存入cookie中
		Cookie cookie = new Cookie("cart", cartJson);
		cookie.setMaxAge(60 * 60 * 24 * 7);// 一周
		response.addCookie(cookie);
	}

	

	// 合并购物车
	public Cart mergeCart(Cart cart1, Cart cart2) {
		if (cart1 == null) {
			return cart2;
		} else if (cart2 == null) {
			return cart1;
		} else {
			// 取出cart2中的购物项
			List<Item> items = cart2.getItems();

			// 将cart2的购物项加入到cart1中
			for (Item item : items) {
				cart1.addItem(item);
			}
			return cart1;
		}
	}

	/**
	 * 删除cookie中的购物车
	 * 
	 * @param request
	 * @param response
	 */
	public void delCartFormCookies(HttpServletRequest request,
			HttpServletResponse response) {
		Cookie[] cookies = request.getCookies();
		if (cookies != null && cookies.length > 0) {
			for (Cookie cookie : cookies) {
				if (cookie.getName().equals("cart")) {
					cookie.setMaxAge(0);
					response.addCookie(cookie);
				}
			}
		}
	}

}
