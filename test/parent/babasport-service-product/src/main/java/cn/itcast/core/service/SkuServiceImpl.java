package cn.itcast.core.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.abel533.entity.Example;

import cn.itcast.core.dao.SkuDAO;
import cn.itcast.core.pojo.Sku;
import cn.itcast.core.pojo.SuperPojo;

/**
 * 库存管理服务实现类
 * 
 * @author Administrator
 *
 */
@Service("skuService")
public class SkuServiceImpl implements SkuService {

	@Autowired
	private SkuDAO skuDAO;

	@Override
	public List<SuperPojo> findByProductId(Long productId) {

		List<SuperPojo> skus = skuDAO.findByProductIdAndColor(productId);
		return skus;

		/*
		 * Example example = new Example(Sku.class);
		 * example.createCriteria().andEqualTo("productId", productId);
		 * 
		 * List<Sku> selectByExample = skuDAO.selectByExample(example);
		 * 
		 * return selectByExample;
		 */
	}

	@Override
	public int update(Sku sku) {

		// 只修改传输数据中非空的内容
		int num = skuDAO.updateByPrimaryKeySelective(sku);
		return num;
	}

	@Override
	public SuperPojo findSkuAndColorAndProductBySkuId(Long skuId) {
		return skuDAO.findSkuAndColorAndProductBySkuId(skuId);
	}

}
