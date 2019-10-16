package com.hzq.interceptors;

import com.hzq.common.Const;
import com.hzq.dao.UserDao;
import com.hzq.execption.CustomGenericException;
import com.hzq.domain.User;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

public class AuthInterceptor extends HandlerInterceptorAdapter {

	private static final Logger LOGGER = LoggerFactory.getLogger(AuthInterceptor.class);
	private List<String> exceptUrls;
	@Autowired
	private UserDao userDao;

		@Override
		@ExceptionHandler(CustomGenericException.class)
		public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
			LOGGER.debug("到达权限拦截器");
			String requestUri = request.getRequestURI();  //得到项目名开始的路径 /test/test.jsp
			if(requestUri.startsWith(request.getContextPath())){ //项目名 /test
				requestUri = requestUri.substring(request.getContextPath().length()); //得到 /test.jsp
			}
			LOGGER.debug("拦截的路径："+requestUri);
			//系统根目录
			if (StringUtils.equals("/",requestUri)) {
				return true;
			}
			//放行exceptUrls中配置的url
			if (exceptUrls != null) {
				for (String url : exceptUrls) {
					if (url.endsWith("/**")) {
						if (requestUri.startsWith(url.substring(0, url.length() - 3))) {
							return true;
						}
					} else if (requestUri.startsWith(url)) {
						return true;
					}
				}
			}
			//其他需要登录后才能进行访问的url
			User user = (User) request.getSession().getAttribute(Const.CURRENT_USER);
			if (null == user ) {
				LOGGER.debug("该用户尚未登录");
				throw CustomGenericException.CreateException(-20, "该用户尚未登录");
			}
			return true;
	}

     @Override
     public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView){
		    }


     @Override
     public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
	 }

	public List<String> getExceptUrls() {
		return exceptUrls;
	}
	public void setExceptUrls(List<String> exceptUrls) {
		this.exceptUrls = exceptUrls;
	}
}