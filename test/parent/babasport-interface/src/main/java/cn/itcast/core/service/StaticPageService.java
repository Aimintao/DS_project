package cn.itcast.core.service;

import java.io.IOException;
import java.util.Map;

/**
 * 静态化服务接口
 * @author Administrator
 *
 */
public interface StaticPageService {
	
	/**
	 * 进行静态化
	 * @param map 数据模型
	 * @param id 商品id （静态文件的名称）
	 * @throws IOException 
	 */
	public void staticProductPage(Map<String, Object> map,String id) throws IOException;

}
