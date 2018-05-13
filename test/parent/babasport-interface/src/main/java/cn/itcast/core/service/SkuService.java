package cn.itcast.core.service;

import java.util.List;

import cn.itcast.core.pojo.Sku;
import cn.itcast.core.pojo.SuperPojo;

/**
 * SKU服务类接口
 * @author Administrator
 *
 */
public interface SkuService {
	
	/**
	 * 根据商品id查询库存
	 * @param productId
	 * @return
	 */
	public List<SuperPojo> findByProductId(Long productId);
	
	/**
	 * 修改库存
	 * @param sku
	 * @return
	 */
	public int update(Sku sku);
	
	/**
	 * 根据库存id查询该库存的信息，并加载颜色名称，和商品名称
	 * 
	 * @param productId
	 * @return
	 */
	public SuperPojo findSkuAndColorAndProductBySkuId(Long skuId);

}
