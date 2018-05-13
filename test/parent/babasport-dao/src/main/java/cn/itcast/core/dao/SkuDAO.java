package cn.itcast.core.dao;

import java.util.List;

import com.github.abel533.mapper.Mapper;

import cn.itcast.core.pojo.Sku;
import cn.itcast.core.pojo.SuperPojo;

public interface SkuDAO extends Mapper<Sku> {

	/**
	 * 根据产品id查询该产品所有库存，并加载颜色名称
	 * 
	 * @param productId
	 * @return
	 */
	public List<SuperPojo> findByProductIdAndColor(Long productId);

	/**
	 * 根据库存id查询该库存的信息，并加载颜色名称，和商品名称
	 * 
	 * @param productId
	 * @return
	 */
	public SuperPojo findSkuAndColorAndProductBySkuId(Long skuId);

}
