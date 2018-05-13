package cn.itcast.core.service;

import java.util.List;

import cn.itcast.core.pojo.Brand;
import cn.itcast.core.tools.PageHelper.Page;

/**
 * 品牌服务类接口
 * @author Administrator
 *
 */
public interface BrandService {
	
	/**
	 * 根据样板条件查询所有品牌
	 * 
	 * @return
	 */
	public Page findByExample(Brand brand,Integer pageNum, Integer pageSize);
	
	/**
	 * 根据品牌id查出品牌对象
	 * 
	 * @param brandId
	 * @return
	 */
	public Brand findById(Long brandId);
	
	/**
	 * 根据id修改品牌
	 * 
	 * @param brand
	 */
	public void updateById(Brand brand);
	
	/**
	 * 根据ids删除品牌
	 * @param ids
	 */
	public void deleteByIds(String ids);
	
	/**
	 * 从redis中查询所有品牌
	 * 
	 * @return
	 */
	public List<Brand> findAllFromRedis();

}
