package cn.itcast.core.action;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

import cn.itcast.core.pojo.Cart;
import cn.itcast.core.pojo.Item;
import cn.itcast.core.pojo.Order;
import cn.itcast.core.service.CartService;
import cn.itcast.core.service.OrderService;
import cn.itcast.core.service.SessionService;
import cn.itcast.core.tools.SessionTool;

/**
 * 订单操作控制器
 * 
 * @author Administrator
 *
 */
@Controller
public class OrderAction {

	@Autowired
	private SessionService sessionService;

	@Autowired
	private CartService cartService;

	@Autowired
	private OrderService orderService;

	// 显示结算 skuIds表示用户从购物车中选择的商品，本次课程先不做该功能
	@RequestMapping(value = "/buyer/trueBuy")
	public String trueBuy(Model model, Long[] skuIds,
			HttpServletRequest request, HttpServletResponse response)
			throws JsonParseException, JsonMappingException, IOException {

		// 获得当前登录用户的用户名
		String username = sessionService.getUsernameForRedis(
				SessionTool.getSessionID(request, response));

		// 判断redis里的购物车不能为空，也不能是空车子
		Cart cart = cartService.getCartFormRedis(username);
		if (cart != null && cart.getItems().size() > 0) {
			// 填充购物车 复合信息（具体库存相关信息）
			cart = cartService.fillItemsSkus(cart);

			Boolean isHave = true; // 有货无货标识

			// 判断库存够不够
			List<Item> items = cart.getItems();
			for (Item item : items) {
				// 购买数量大于库存数量
				if (item.getAmount() > (Integer) item.getSku().get("stock")) {
					item.setIsHave(false);// 设置该商品无货
					isHave = false;
				}
			}
			// 至少有一件商品没有货
			if (!isHave) {
				model.addAttribute("cart", cart);
				return "cart";
			}
		} else {
			return "redirect:/cart";
		}

		System.out.println("显示订单结算页面");
		return "order";
	}

	// 提交订单
	@RequestMapping(value = "/buyer/submitOrder")
	public String submitOrder(Order order, HttpServletRequest request,
			HttpServletResponse response)
			throws JsonParseException, JsonMappingException, IOException {
		String username = sessionService.getUsernameForRedis(
				SessionTool.getSessionID(request, response));

		//保存订单及订单详情
		orderService.addOrderAndDetail(order, username);

		return "success";
	}

}
