package cn.itcast.core.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import cn.itcast.core.service.SessionService;
import cn.itcast.core.tools.SessionTool;

/**
 * 自定义拦截器
 * 
 * @author Administrator
 *
 */
public class MyInterceptor implements HandlerInterceptor {

	@Autowired
	private SessionService sessionService;

	// 前置
	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {

		System.out.println("判断用户有没有登录");

		// 判断用户有没有登录
		String username = sessionService.getUsernameForRedis(
				SessionTool.getSessionID(request, response));

		if (username == null) {
			// 重定向到登录页面，当用户再次登录后，即可回到购物车页面
			response.sendRedirect(
					"http://localhost:8081/login.aspx?returnUrl=http://localhost:8082/cart");

			return false;
		}
		return true;
	}

	// 后置
	@Override
	public void postHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		// TODO Auto-generated method stub

	}

	// 渲染视图后
	@Override
	public void afterCompletion(HttpServletRequest request,
			HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		// TODO Auto-generated method stub

	}

}
