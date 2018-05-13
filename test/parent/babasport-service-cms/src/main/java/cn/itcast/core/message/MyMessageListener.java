package cn.itcast.core.message;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;

import org.apache.activemq.command.ActiveMQTextMessage;
import org.springframework.beans.factory.annotation.Autowired;

import cn.itcast.core.pojo.Color;
import cn.itcast.core.pojo.SuperPojo;
import cn.itcast.core.service.ProductService;
import cn.itcast.core.service.StaticPageService;

/**
 * 静态化的消息服务监听类
 * 
 * @author Administrator
 *
 */
public class MyMessageListener implements MessageListener {

	@Autowired
	private StaticPageService staticPageService;
	
	@Autowired
	private ProductService productService;

	@Override
	public void onMessage(Message message) {
		ActiveMQTextMessage amessage = (ActiveMQTextMessage) message;

		try {
			String ids = amessage.getText();
			System.out.println("CMS服务接收到的ids:" + ids);

			// 分割ids 并遍历
			String[] split = ids.split(",");
			for (String id : split) {
	
				long productId = Long.parseLong(id);
				
				System.out.println("商品id:" + id);

				// 根据商品id查询单个商品信息 (带有该商品所有库存信息)
				SuperPojo superPojo = productService.findById(productId);

				System.out.println("商品对象：" + superPojo.get("product"));
				List skus = (List) superPojo.get("skus");
				System.out.println("sku的数量：" + skus.size());
				
				HashSet<Color> colors = new HashSet<Color>();

				for (Object object : skus) {
					SuperPojo sku = (SuperPojo) object;
					
					Color color = new Color();
					color.setId((Long)sku.get("color_id"));
					color.setName((String) sku.get("colorName"));
					colors.add(color);
				}

				// 反正万能实体对象要被传递，将非重复的颜色对象也通过superPojo顺便传递到页面
				superPojo.setProperty("colors", colors);
				
				HashMap<String, Object> hashMap = new HashMap<String, Object>();
				hashMap.put("superPojo", superPojo);
				
				//执行静态化
				staticPageService.staticProductPage(hashMap, id);
			}

		} catch (JMSException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
