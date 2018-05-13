package cn.itcast.core.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.itcast.core.pojo.SuperPojo;
import cn.itcast.core.service.ProductService;

/**
 * 商品管理控制器
 * @author Administrator
 *
 */
@Controller
public class ProductAction {
	
	@Autowired
	private ProductService productService;
	
	//显示单个商品的详情页面
	@RequestMapping(value="/product/detail")
	public String detail(Model model,Long productId)
	{
		System.out.println("商品id:"+productId);
		
		//根据商品id查询单个商品信息 (带有该商品所有库存信息)
		SuperPojo superPojo = productService.findById(productId);
		
		System.out.println("商品对象："+superPojo.get("product"));
		List skus = (List)superPojo.get("skus");
		System.out.println("sku的数量："+skus.size());
		
		Map colors = new HashMap();
		
		for (Object object : skus) {
			SuperPojo sku =  (SuperPojo)object;
			colors.put((Long)sku.get("color_id"), (String)sku.get("colorName"));
		}
		
		// 反正万能实体对象要被传递，将非重复的颜色对象也通过superPojo顺便传递到页面
		superPojo.setProperty("colors", colors);
	
		model.addAttribute("superPojo", superPojo);
		
		return "product";
	}

}
