package cn.itcast.core.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrQuery.ORDER;
import org.apache.solr.client.solrj.SolrQuery.SortClause;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.abel533.entity.Example;

import cn.itcast.core.tools.PageHelper;
import cn.itcast.core.tools.PageHelper.Page;
import cn.itcast.core.dao.ProductDAO;
import cn.itcast.core.dao.SkuDAO;
import cn.itcast.core.pojo.Product;
import cn.itcast.core.pojo.Sku;
import cn.itcast.core.pojo.SuperPojo;

@Service("solrService")
public class SolrServiceImpl implements SolrService {

	@Autowired
	private HttpSolrServer solrServer;

	@Autowired
	private ProductDAO productDAO;

	@Autowired
	private SkuDAO skuDAO;

	@Override
	public Page<SuperPojo> findProductByKeyWord(String keyword, String sort,
			Integer pageNum, Integer pageSize, Long brandId, Float pa, Float pb)
			throws SolrServerException {
		// 设置查询关键字
		SolrQuery solrQuery = new SolrQuery("name_ik:" + keyword);

		if (brandId != null) {
			// 添加过滤条件
			solrQuery.addFilterQuery("brandId:" + brandId);
		}

		if (pa != null && pb != null) {
			if (pb == -1) {
				// 添加价格区间
				solrQuery.addFilterQuery("price:[" + pa + " TO *]");
			} else {
				// 添加价格区间
				solrQuery.addFilterQuery("price:[" + pa + " TO " + pb + "]");
			}

		}

		// 设置高亮
		solrQuery.setHighlight(true);// 开始高亮
		solrQuery.addHighlightField("name_ik");// 添加高亮结果的列
		solrQuery.setHighlightSimplePre("<span style='color:red'>");// 前缀
		solrQuery.setHighlightSimplePost("</span>");// 后缀

		// solrQuery.setRows(100);

		// 设置排序
		// solrQuery.setSort("price", ORDER.asc);
		// solrQuery.setSort(new SortClause("price", "asc"));
		solrQuery.setSort(
				new SortClause(sort.split(" ")[0], sort.split(" ")[1]));

		// 设置分页

		// 开始分页设置
		Page<SuperPojo> page = new Page<SuperPojo>(pageNum, pageSize);
		solrQuery.setStart(page.getStartRow());
		solrQuery.setRows(page.getPageSize());

		// 开始查询，并返回查询相应对象
		QueryResponse response = solrServer.query(solrQuery);

		// 获得高亮的结果数据 大map是id 小map key 列名 value xxx
		Map<String, Map<String, List<String>>> highlighting = response
				.getHighlighting();

		// 通过响应对象获得查询结果
		SolrDocumentList results = response.getResults();

		// 获得总数量
		long total = results.getNumFound();
		page.setTotal(total);

		// 将结果集中的信息封装到商品对象中
		// 注意：由于原商品对象中并没有价格属性，而价格属性本应该是在商品对象的子对象库存对象中，
		// 而本次设计并不打算使用类似于hibernate的在pojo中做对象的相应关联，所以这里，我们可以使用万能对象来装载数据
		// 一个万能对象就可以等同于从数据库查询（包括连接查询）出的结果表中的一条数据

		// 超级商品对象的集合
		List<SuperPojo> al = new ArrayList<SuperPojo>();

		for (SolrDocument solrDocument : results) {

			// 创建商品对象
			SuperPojo superProduct = new SuperPojo();

			// 商品id
			String id = (String) solrDocument.get("id");
			superProduct.setProperty("id", id);

			// 商品的名称
			// String name = (String) solrDocument.get("name_ik");
			// superProduct.setProperty("name", name);

			// 将商品名称变为高亮后的数据
			Map<String, List<String>> map = highlighting.get(id);
			String string = map.get("name_ik").get(0);
			superProduct.setProperty("name", string);

			// 商品的图片
			String imgUrl = (String) solrDocument.get("url");
			superProduct.setProperty("imgUrl", imgUrl);

			// 商品的品牌id
			String brandId2 = (String) solrDocument.get("brandId");
			superProduct.setProperty("brandId", brandId2);

			// 商品的最低价格
			Float price = (Float) solrDocument.get("price");
			superProduct.setProperty("price", price);

			al.add(superProduct);
		}

		page.setResult(al);

		return page;
	}

	@Override
	public void addProduct(String ids) throws SolrServerException, IOException {

		// 将ids的字符串转成list集合
		List<Object> al = new ArrayList<Object>();
		String[] split = ids.split(",");
		for (String string : split) {
			al.add(string);
		}

		Example example = new Example(Product.class);
		// 设置 批量修改的id条件 (或商品查询)
		example.createCriteria().andIn("id", al);

		// 1、商品信息存到solr索引库中 ids
		List<Product> products = productDAO.selectByExample(example); // 1

		// 遍历查询出来的商品集合
		for (Product product2 : products) { // 5

			// 将商品的各个信息，添加到文档对象中
			SolrInputDocument solrInputDocument = new SolrInputDocument();
			solrInputDocument.addField("id", product2.getId());
			solrInputDocument.addField("name_ik", product2.getName());
			solrInputDocument.addField("url",
					product2.getImgUrl().split(",")[0]);
			solrInputDocument.addField("brandId", product2.getBrandId());

			// 库存中的最低价
			Example example2 = new Example(Sku.class);
			// 根据商品id找出库存
			example2.createCriteria().andEqualTo

			("productId", product2.getId()); // 5
			// 价格升序
			example2.setOrderByClause("price asc");

			// limit
			PageHelper.startPage(1, 1);
			skuDAO.selectByExample(example2);// 5
			Page pageSku = PageHelper.endPage();
			Sku sku = (Sku) pageSku.getResult().get

			(0);// 最低价格的库存

			// 将某商品的最低价格添加到solr中
			solrInputDocument.addField("price",

					sku.getPrice());

			solrServer.add(solrInputDocument);

			solrServer.commit();

		}

	}

}
