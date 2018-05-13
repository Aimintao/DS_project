package cn.itcast.core.action;

import java.util.HashMap;
import java.util.Map;

import javax.jms.Session;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;



import cn.itcast.core.pojo.Buyer;
import cn.itcast.core.service.BuyerService;
import cn.itcast.core.service.SessionService;
import cn.itcast.core.tools.Encoding;
import cn.itcast.core.tools.Encryption;
import cn.itcast.core.tools.SessionTool;

/**
 * 登录管理控制器
 * 
 * @author Administrator
 *
 */
@Controller
public class LoginAction {

	@Autowired
	private BuyerService buyerService;

	@Autowired
	private SessionService sessionService;

	// 显示登录 只接收get请求
	@RequestMapping(value = "/login.aspx", method = RequestMethod.GET)
	public String showLogin() {
		System.out.println("get");
		return "login";
	}

	// 执行登录 只接收post请求
	@RequestMapping(value = "/login.aspx", method = RequestMethod.POST)
	public String doLogin(Model model, String returnUrl, String username,
			String password, HttpServletRequest request,
			HttpServletResponse response) {
		System.out.println("post");
		System.out.println(username);
		System.out.println(password);
		// returnUrl = Encoding.encodeGetRequest(returnUrl);
		System.out.println(returnUrl);

		if (returnUrl == null || returnUrl.length() < 1) {
			returnUrl = "http://localhost:8082/";
		}

		String error = "";

		if (username != null) {

			if (password != null) {
				Buyer buyer = buyerService.findByUsername(username);
				System.out.println(buyer);

				if (buyer != null) {

					// 判断密码
					if (Encryption.encrypt(password)
							.equals(buyer.getPassword())) {
						// 密码正确

						// session(redis)的相关操作
						String maosessionid = SessionTool.getSessionID(request,
								response);

						// 将用户名保存到自定义session中(redis)
						sessionService.addUsernameToRedis(maosessionid,
								username);

						// 回到登录之前的页面
						return "redirect:" + returnUrl;

					} else {
						error = "密码不正确";
					}

				} else {
					error = "用户名不存在";
				}
			} else {
				error = "密码不能为空";
			}

		} else {
			error = "用户名不能为空";
		}

		model.addAttribute("error", error);
		return "login";
	}

	// 检查用户是否登录
	@RequestMapping(value = "/isLogin.aspx")
	@ResponseBody
	public MappingJacksonValue isLogin(String callback,HttpServletRequest request,
			HttpServletResponse response) {
		
		System.out.println("callback:"+callback);

		// 获得maosessionid request
		String maosessionid = SessionTool.getSessionID(request, response);

		// 从redis中取出用户名
		String username = sessionService.getUsernameForRedis(maosessionid);

		System.out.println("username" + username);
	
		MappingJacksonValue mappingJacksonValue = new MappingJacksonValue(username);
		mappingJacksonValue.setJsonpFunction(callback);
		
		//jQuery6372406("fbb2016");
	
		return mappingJacksonValue;
	}

}
