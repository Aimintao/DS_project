package cn.itcast.core.action;

import java.io.IOException;
import java.util.List;

import org.apache.solr.client.solrj.SolrServerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.itcast.core.pojo.Brand;
import cn.itcast.core.pojo.Color;
import cn.itcast.core.pojo.Product;
import cn.itcast.core.service.BrandService;
import cn.itcast.core.service.ProductService;
import cn.itcast.core.tools.Encoding;
import cn.itcast.core.tools.PageHelper.Page;

/**
 * 商品管理控制器
 * 
 * @author Administrator
 *
 */
@Controller
public class ProductAction {

	@Autowired
	private ProductService productService;

	@Autowired
	private BrandService brandService;

	// 商品页面(通用)
	@RequestMapping(value = "/console/product/{pageName}.do")
	public String consoleProductShow(Model model,
			@PathVariable(value = "pageName") String pageName) {
		return "/product/" + pageName;
	}

	// 显示商品列表
	@RequestMapping(value = "/console/product/list.do")
	public String consoleProductShowList(Model model, String name, Long brandId,
			Integer isShow, Integer pageNum, Integer pageSize) {

		// 接收商品名称（查询条件）
		System.out.println("商品名称" + name);
		name = Encoding.encodeGetRequest(name);
		Product product = new Product();
		product.setName(name);

		Page pageProduct = productService.findByExample(product, pageNum,
				pageSize);
		System.out.println(pageProduct.getResult().size());

		// 加载所有品牌
		Page pageBrand = brandService.findByExample(null, 1, 200);

		model.addAttribute("pageProduct", pageProduct);

		// 将品牌信息传递给页面
		model.addAttribute("brands", pageBrand.getResult());

		return "/product/list";
	}

	// 显示商品添加
	@RequestMapping(value = "/console/product/showAdd.do")
	public String consoleProductShowEdit(Model model) {

		// 加载所有品牌
		Page pageBrand = brandService.findByExample(null, 1, 200);
		// 将品牌信息传递给页面
		model.addAttribute("brands", pageBrand.getResult());

		// 加载可用颜色
		List<Color> colors = productService.findEnableColors();
		model.addAttribute("colors", colors);

		return "/product/add";
	}

	// 执行商品添加
	@RequestMapping(value = "/console/product/doAdd.do")
	public String consoleProductDoAdd(Model model, Product product) {
		System.out.println(product);
		productService.add(product);

		return "redirect:list.do";
	}

	// 执行商品上下架
	@RequestMapping(value = "/console/product/isShow.do")
	public String consoleProductDoIsShow(String ids,Integer isShow) throws SolrServerException, IOException {
	
		System.out.println("商品id集合："+ids);
		System.out.println("上下架："+isShow);
		
		Product product = new Product();
		product.setIsShow(isShow);
		productService.update(ids, product);
		
		return "redirect:list.do";
	}

}
