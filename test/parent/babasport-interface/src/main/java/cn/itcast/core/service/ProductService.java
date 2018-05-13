package cn.itcast.core.service;

import java.io.IOException;
import java.util.List;

import org.apache.solr.client.solrj.SolrServerException;

import cn.itcast.core.pojo.Brand;
import cn.itcast.core.pojo.Color;
import cn.itcast.core.pojo.Product;
import cn.itcast.core.pojo.SuperPojo;
import cn.itcast.core.tools.PageHelper.Page;

/**
 * 商品服务类接口
 * 
 * @author Administrator
 *
 */
public interface ProductService {

	/**
	 * 根据样板条件查询所有商品
	 * 
	 * @return
	 */
	public Page findByExample(Product product, Integer pageNum,
			Integer pageSize);
	
	/**
	 * 查询所有可用颜色（颜色的父id不为0）
	 * 
	 * @return
	 */
	public List<Color> findEnableColors();
	
	/**
	 * 添加商品
	 * @param product
	 */
	public void add(Product product);
	
	/**
	 * 修改很多商品的统一信息
	 * 
	 * @param ids 商品的id集合
	 * @param product 模版
	 * @throws IOException 
	 * @throws SolrServerException 
	 */
	public void update(String ids,Product product) throws SolrServerException, IOException;

	
	/**
	 * 根据商品id查询单个商品信息 (带有该商品所有库存信息)
	 * @param id
	 * @return sku product 
	 */
	public SuperPojo findById(Long id);
	
}
