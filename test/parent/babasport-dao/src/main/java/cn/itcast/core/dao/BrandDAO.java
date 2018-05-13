package cn.itcast.core.dao;

import java.util.List;

import cn.itcast.core.pojo.Brand;

public interface BrandDAO {

	/**
	 * 根据样板条件查询所有品牌
	 * 
	 * @return
	 */
	public List<Brand> findByExample(Brand brand);

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

}
