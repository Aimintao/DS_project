package cn.itcast.core.service;

import java.io.IOException;
import java.util.List;

import org.apache.solr.client.solrj.SolrServerException;

import cn.itcast.core.pojo.Product;
import cn.itcast.core.pojo.SuperPojo;
import cn.itcast.core.tools.PageHelper.Page;

/**
 * solr服务接口
 * 
 * @author Administrator
 *
 */
public interface SolrService {

	/**
	 * 根据关键字查询商品信息（* solr）
	 * 
	 * @param keyword
	 * @param sort
	 * @param pageNum
	 * @param pageSize
	 * @param brandId
	 * @param pa  价格起点
	 * @param pb  价格终点
	 * @return
	 * @throws SolrServerException
	 */
	public Page<SuperPojo> findProductByKeyWord(String keyword, String sort,
			Integer pageNum, Integer pageSize,Long brandId,Float pa,Float pb) throws SolrServerException;

	/**
	 * 添加商品到solr服务器中
	 * 
	 * @param ids
	 * @throws SolrServerException
	 * @throws IOException
	 */
	public void addProduct(String ids) throws SolrServerException, IOException;
	
}
