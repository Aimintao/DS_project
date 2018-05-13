package cn.itcast.core.action;

import java.util.List;
import java.util.TreeMap;

import org.apache.solr.client.solrj.SolrServerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.itcast.core.pojo.Brand;
import cn.itcast.core.pojo.SuperPojo;
import cn.itcast.core.service.BrandService;
import cn.itcast.core.service.SolrService;
import cn.itcast.core.tools.Encoding;
import cn.itcast.core.tools.PageHelper.Page;

/**
 * 首页管理控制器
 * 
 * @author Administrator
 *
 */
@Controller
public class IndexAction {

	@Autowired
	private SolrService solrService;

	@Autowired
	private BrandService brandService;

	// 显示首页
	@RequestMapping(value = "/")
	public String index() {
		return "index";
	}

	// 首页搜索
	@RequestMapping(value = "/search")
	public String indexSearch(Model model, String keyword, String sort,
			Integer pageNum, Integer pageSize, Long brandId, Float pa, Float pb)
			throws SolrServerException {
		keyword = Encoding.encodeGetRequest(keyword);
		System.out.println(keyword);

		// 如果没有排序规则，则价格升序
		if (sort == null || sort.equals("undefined") || sort.length() == 0) {
			sort = "price asc";
		}

		Page<SuperPojo> pageProducts = solrService.findProductByKeyWord(keyword,
				sort, pageNum, pageSize, brandId, pa, pb);

		System.out.println("超级商品：" + pageProducts.getResult().size());

		model.addAttribute("pageProducts", pageProducts);
		model.addAttribute("keyword", keyword);

		model.addAttribute("sort2", sort);

		// 反过来sort price asc == price desc
		if (sort.equals("price desc")) {
			sort = "price asc";
		} else if (sort.equals("price asc")) {
			sort = "price desc";
		}

		model.addAttribute("sort", sort);

		// 从redis中查询所有品牌 并传给页面
		List<Brand> brands = brandService.findAllFromRedis();
		model.addAttribute("brands", brands);

		// 将brandId回显给页面
		model.addAttribute("brandId", brandId);

		// 将用户选择的价格区间 回显给页面
		model.addAttribute("pa", pa);
		model.addAttribute("pb", pb);

		// 构建已选条件的map
		TreeMap<String, String> treeMap = new TreeMap<String, String>();

		// 品牌
		if (brandId != null) {
			for (Brand brand : brands) {
				if (brand.getId() == brandId) {
					treeMap.put("品牌", brand.getName());
					break;
				}
			}
		}

		//价格
		if (pa != null && pb != null) {
			if(pb==-1)
			{
				treeMap.put("价格", pa + "以上");
			}
			else
			{
				treeMap.put("价格", pa + "-" + pb);
			}
			
		}

		model.addAttribute("map", treeMap);

		return "search";
	}

}
