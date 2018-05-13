package cn.itcast.core.service;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.Map;

import javax.servlet.ServletContext;

import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.web.context.ServletContextAware;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

/**
 * 静态化服务器实现类
 * 
 * @author Administrator
 *
 */
@Service
public class StaticPageServiceImpl
		implements StaticPageService, ServletContextAware {

	@Override
	public void staticProductPage(Map<String, Object> map, String id)
			throws IOException {

		// 配置器
		Configuration configuration = new Configuration();

		// 模版文件目录
		String mPath = servletContext.getRealPath("/ftl");
		System.out.println("mPath" + mPath);

		// 生成的静态文件位置
		String hpath = servletContext
				.getRealPath("/html/product/" + id + ".html");
		System.out.println("hpath" + hpath);

		// 获得最终文件的父文件（目录）
		File file = new File(hpath);
		File parentFile = file.getParentFile();

		// 如果父目录不存在，则进行创建
		if (!parentFile.exists()) {
			parentFile.mkdir();
		}

		// 设置模版目录
		configuration.setDirectoryForTemplateLoading(new File(mPath));

		// 加载模版文件
		Template template = configuration.getTemplate("product.html");

		// 设置输出文件的位置
		Writer out = new FileWriter(new File(hpath));

		// 开始输出
		try {
			template.process(map, out);
		} catch (TemplateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private ServletContext servletContext;

	@Override
	public void setServletContext(ServletContext servletContext) {
		this.servletContext = servletContext;
	}

}
