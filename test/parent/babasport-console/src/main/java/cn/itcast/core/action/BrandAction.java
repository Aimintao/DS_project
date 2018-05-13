package cn.itcast.core.action;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.itcast.core.pojo.Brand;
import cn.itcast.core.service.BrandService;
import cn.itcast.core.tools.Encoding;
import cn.itcast.core.tools.PageHelper.Page;

/**
 * 品牌管理控制器
 * 
 * @author Administrator
 *
 */
@Controller
public class BrandAction {

	@Autowired
	private BrandService brandService;

	// 通用品牌页面
	@RequestMapping(value = "/console/brand/{pageName}.do")
	public String consoleBrandShow(Model model,
			@PathVariable(value = "pageName") String pageName) {

		System.out.println("xixi");

		return "/brand/" + pageName;
	}

	// 显示品牌列表
	@RequestMapping(value = "/console/brand/list.do")
	public String consoleBrandShowList(Model model,
			String name, Integer isDisplay, Integer pageNum, Integer pageSize) {

		System.out.println("haha");

		// 接收查询条件数据
		System.out.println(name);
		System.out.println(isDisplay);

		// 将查询条件封装到Brand对象中
		Brand brand = new Brand();
		// 处理get方式编码
		name = Encoding.encodeGetRequest(name);
		brand.setName(name);
		brand.setIsDisplay(isDisplay);

		// 根据条件查询品牌
		Page pageBrand = brandService.findByExample(brand, pageNum, pageSize);

		System.out.println(pageBrand.getResult().size());

		// 将查询的条件进行回显
		model.addAttribute("name", name);
		model.addAttribute("isDisplay", isDisplay);

		model.addAttribute("pageBrand", pageBrand);

		return "/brand/list";
	}

	// 显示品牌编辑
	@RequestMapping(value = "/console/brand/showEdit.do")
	public String consoleBrandShowEdit(Model model, Long brandId) {
		System.out.println(brandId);

		// 根据品牌id查出品牌对象
		Brand brand = brandService.findById(brandId);
		System.out.println(brand);

		model.addAttribute("brand", brand);

		return "/brand/edit";
	}

	// 进行品牌编辑
	@RequestMapping(value = "/console/brand/doEdit.do")
	public String consoleBrandDoEdit(Model model, Brand brand) {
		System.out.println(brand);
		brandService.updateById(brand);

		return "redirect:list.do";
	}

	// 进行品牌删除
	@RequestMapping(value = "/console/brand/doDelete.do")
	public String consoleBrandDoDelete(Model model, String ids) {
		System.out.println("ids"+ids);
		
		//根据ids删除品牌
		brandService.deleteByIds(ids);
		
		return "redirect:list.do";
	}

}
