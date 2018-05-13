package cn.itcast.core.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.itcast.core.dao.BrandDAO;
import cn.itcast.core.pojo.Brand;
import cn.itcast.core.tools.PageHelper;
import cn.itcast.core.tools.PageHelper.Page;
import redis.clients.jedis.Jedis;

/**
 * 品牌服务实现类
 * 
 * @author Administrator
 *
 */
@Service("brandService")
public class BrandServiceImpl implements BrandService {

	@Autowired
	private BrandDAO brandDAO;
	
	@Autowired
	private Jedis jedis;

	@Override
	public Page findByExample(Brand brand, Integer pageNum, Integer pageSize) {
		PageHelper.startPage(pageNum, pageSize);// 开始分页
		List<Brand> brands = brandDAO.findByExample(brand);
		Page pageBrand = PageHelper.endPage();
		return pageBrand;
	}

	@Override
	public Brand findById(Long brandId) {
		Brand brand = brandDAO.findById(brandId);
		return brand;
	}

	@Override
	public void updateById(Brand brand) {
		// 将品牌信息同步修改redis
		jedis.hset("brand", String.valueOf(brand.getId()), brand.getName());
		brandDAO.updateById(brand);
	}

	@Override
	public void deleteByIds(String ids) {
		// TODO Auto-generated method stub
		brandDAO.deleteByIds(ids);
	}

	@Override
	public List<Brand> findAllFromRedis() {
		
		Map<String, String> hgetAll = jedis.hgetAll("brand");
		
		// 将查询的结果放入到品牌对象集合中
		List<Brand> brands = new ArrayList<Brand>();		
		Set<Entry<String, String>> entrySet = hgetAll.entrySet();
		for (Entry<String, String> entry : entrySet) {
			Brand brand = new Brand();
			brand.setId(Long.parseLong(entry.getKey()));
			brand.setName(entry.getValue());
			brands.add(brand);
		}
		
		return brands;
	}

}
