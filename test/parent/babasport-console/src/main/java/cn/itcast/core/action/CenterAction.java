package cn.itcast.core.action;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.itcast.core.pojo.TestTb;
import cn.itcast.core.service.TestTbService;

/**
 * 后台管理控制中心
 * 
 * @author Administrator
 *
 */
@Controller
public class CenterAction {

	@Autowired
	private TestTbService testTbService;

	// 总
	@RequestMapping(value = "/console/{pageName}.do")
	public String index(Model model,
			@PathVariable(value = "pageName") String pageName) {

		return pageName;
	}

	// 框架页面
	@RequestMapping(value = "/console/frame/{pageName}.do")
	public String consoleFrameShow(Model model,
			@PathVariable(value = "pageName") String pageName) {
		return "/frame/" + pageName;
	}

	
	

}
