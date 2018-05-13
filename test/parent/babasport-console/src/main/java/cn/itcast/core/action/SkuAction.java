package cn.itcast.core.action;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.itcast.core.pojo.Sku;
import cn.itcast.core.pojo.SuperPojo;
import cn.itcast.core.service.SkuService;

/**
 * 库存管理控制器
 * 
 * @author Administrator
 *
 */
@Controller
public class SkuAction {

	@Autowired
	private SkuService skuService;

	// 显示库存管理
	@RequestMapping(value = "/console/sku/list.do")
	public String consoleSkuShowList(Model model, Long productId) {
		System.out.println("用户显示库存的商品id：" + productId);

		List<SuperPojo> skus = skuService.findByProductId(productId);
		System.out.println(skus.size());
		model.addAttribute("skus", skus);

		return "/sku/list";
	}

	// 修改库存
	@RequestMapping(value = "/console/sku/update.do")
	@ResponseBody
	public String consoleSkuDoUpdate(Model model,Sku sku) {
		
		System.out.println(sku);
		int num = skuService.update(sku);
		return num+"";
	}

}
