package cn.itcast.core.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.abel533.entity.Example;

import cn.itcast.core.dao.ColorDAO;
import cn.itcast.core.dao.ProductDAO;
import cn.itcast.core.dao.SkuDAO;
import cn.itcast.core.pojo.Color;
import cn.itcast.core.pojo.Product;
import cn.itcast.core.pojo.Sku;
import cn.itcast.core.pojo.SuperPojo;
import cn.itcast.core.tools.PageHelper;
import cn.itcast.core.tools.PageHelper.Page;
import redis.clients.jedis.Jedis;

/**
 * 商品服务实现类
 * 
 * @author Administrator
 *
 */
@Service("productService")
@Transactional
public class ProductServiceImpl implements ProductService {

	@Autowired
	private ProductDAO productDAO;

	@Autowired
	private ColorDAO colorDAO;

	@Autowired
	private SkuDAO skuDAO;

	@Autowired
	private Jedis jedis;

	@Autowired
	private HttpSolrServer solrServer;

	@Autowired
	private JmsTemplate jmsTemplate;

	@Override
	public Page findByExample(Product product, Integer pageNum,
			Integer pageSize) {

		// 加入like条件
		Example example = new Example(Product.class);
		if (product.getName() == null) {
			product.setName("");
		}
		example.createCriteria().andLike("name", "%" + product.getName() + "%");
		example.setOrderByClause("createTime desc");// 根据时间倒叙来排
		PageHelper.startPage(pageNum, pageSize);// 开始分页
		productDAO.selectByExample(example);
		Page pageProduct = PageHelper.endPage();// 结束分页

		return pageProduct;
	}

	@Override
	public List<Color> findEnableColors() {
		Example example = new Example(Color.class);
		example.createCriteria().andNotEqualTo("parentId", 0);
		List<Color> colors = colorDAO.selectByExample(example);
		return colors;
	}

	@Override
	public void add(Product product) {
		if (product.getIsShow() == null) {
			product.setIsShow(0);// 下架
		}
		product.setCreateTime(new Date());

		// 开始添加商品

		// 从redis中获得商品id
		Long incr = jedis.incr("pno");
		product.setId(incr);

		productDAO.insert(product);
		System.out.println("商品id：" + product.getId());

		// 开始添加库存
		// 将商品信息添加到库存表中
		// 遍历不同的颜色和尺码
		// 每一个不同颜色，或者不同尺码，都应该插入库存表中，成为一条数据
		String[] colors = product.getColors().split(",");
		String[] sizes = product.getSizes().split(",");

		for (String color : colors) {

			for (String size : sizes) {

				Sku sku = new Sku();
				sku.setColorId(Long.parseLong(color));
				sku.setSize(size);
				sku.setProductId(product.getId());

				sku.setMarketPrice(1000.00f);
				sku.setPrice(800.00f);
				sku.setDeliveFee(20f);
				sku.setStock(0);
				sku.setUpperLimit(100);
				sku.setCreateTime(new Date());

				skuDAO.insert(sku);
			}
		}
	}

	@Override
	public void update(final String ids, Product product)
			throws SolrServerException, IOException {

		// 将ids的字符串转成list集合
		List<Object> al = new ArrayList<Object>();
		String[] split = ids.split(",");
		for (String string : split) {
			al.add(string);
		}

		Example example = new Example(Product.class);
		// 设置 批量修改的id条件 (或商品查询)
		example.createCriteria().andIn("id", al);

		// 进行批量，选择性的非空属性修改
		productDAO.updateByExampleSelective(product, example);

		// 商品上架
		if (product.getIsShow() == 1) {
			// 采用消息服务模式
			// 将商品信息添加到solr服务器中（发送消息（ids）到ActiveMQ中）
			// 单个商品详情页面的（静态化工作） 数据库 ids
			jmsTemplate.send("productIds", new MessageCreator() {
				@Override
				public Message createMessage(Session session)
						throws JMSException {
					// 使用session创建文本消息
					return session.createTextMessage(ids);
				}
			});

			

		}
	}

	@Override
	public SuperPojo findById(Long id) {
		
		//根据商品id查询单个商品信息
		Product product = productDAO.selectByPrimaryKey(id);
		
		//根据商品id查询库存信息
//		Example example = new Example(Sku.class);
//		example.createCriteria().andEqualTo("productId", id);
//		List<Sku> skus = skuDAO.selectByExample(example);
		
		//根据产品id查询该产品所有库存，并加载颜色名称
		List<SuperPojo> skus = skuDAO.findByProductIdAndColor(id);
		
		SuperPojo superPojo = new SuperPojo();
		superPojo.setProperty("product", product);
		superPojo.setProperty("skus", skus);
		
		return superPojo;
	}

}
