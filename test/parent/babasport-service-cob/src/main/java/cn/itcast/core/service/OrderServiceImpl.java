package cn.itcast.core.service;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

import cn.itcast.core.dao.DetailDAO;
import cn.itcast.core.dao.OrderDAO;
import cn.itcast.core.pojo.Cart;
import cn.itcast.core.pojo.Detail;
import cn.itcast.core.pojo.Item;
import cn.itcast.core.pojo.Order;
import redis.clients.jedis.Jedis;

@Service("orderService")
@Transactional
public class OrderServiceImpl implements OrderService {

	@Autowired
	private OrderDAO orderDAO;

	@Autowired
	private DetailDAO detailDAO;

	@Autowired
	private Jedis jedis;

	@Autowired
	private CartService cartService;

	@Override
	public void addOrderAndDetail(Order order, String username)
			throws JsonParseException, JsonMappingException, IOException {
		// 添加订单信息到数据库中

		// 生成订单id
		Long id = jedis.incr("oid");
		order.setId(id);

		// 从redis中取出购物车
		Cart cart = cartService.getCartFormRedis(username);
		// 填充购物车
		cart = cartService.fillItemsSkus(cart);

		// 从购物车中取出相关信息放入订单对象中
		order.setDeliverFee(cart.getFee());
		order.setTotalPrice(cart.getTotalPrice());
		order.setOrderPrice(cart.getProductPrice());

		// 设置订单的支付状态
		// 支付状态 :0到付 1待付款,2已付款,3待退款,4退款成功,5退款失败
		if (order.getPaymentWay() == 1) {
			order.setIsPaiy(0);
		} else {
			order.setIsPaiy(1);
		}

		// 设置订单状态
		// 订单状态 0:提交订单 1:仓库配货 2:商品出库 3:等待收货 4:完成 5待退货 6已退货
		order.setOrderState(0);

		// 设置时间
		order.setCreateDate(new Date());

		// 设置用户id
		// 前台注册的时候可以将用户名和用户id保存到redis中，key：用户名
		// value：用户id，方便根据用户名获得用户id
		Long buyerId = Long.parseLong(jedis.get(username));
		order.setBuyerId(buyerId);

		orderDAO.insert(order);// 1

		// 获得购物车中的购物项
		List<Item> items = cart.getItems();

		for (Item item : items) {
			Detail detail = new Detail();
			detail.setOrderId(id);

			detail.setProductId(
					Long.parseLong(item.getSku().get("product_id").toString()));
			detail.setProductName(item.getSku().get("productName").toString());
			detail.setColor(item.getSku().get("colorName").toString());
			detail.setSize(item.getSku().get("size").toString());
			detail.setPrice(
					Float.parseFloat(item.getSku().get("price").toString()));
			detail.setAmount(item.getAmount());

			// 添加订单详情（商品信息）到数据库中
			detailDAO.insert(detail);
		}
		// 清空购物车
		jedis.del("cart:" + username);
	}
}
