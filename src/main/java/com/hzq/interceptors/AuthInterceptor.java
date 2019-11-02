package com.hzq.interceptors;

import com.hzq.common.Const;
import com.hzq.enums.ResponseCodeEnum;
import com.hzq.execption.CustomGenericException;
import com.hzq.domain.User;
import com.hzq.utils.JwtUil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AuthInterceptor extends HandlerInterceptorAdapter {

	private static final Logger LOGGER = LoggerFactory.getLogger(AuthInterceptor.class);

	@Override
		public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
			//查看请求路径
			String requestUri = getPath(request);
			//系统根目录,直接放行。
			if (StringUtils.equals("/",requestUri)) {
				return true;
			}
			//其他需要登录后才能进行访问的url
			User user = (User) request.getSession().getAttribute(Const.CURRENT_USER);
			//LOGGER.debug("用户的是否登录user-->"+user);
			if (null == user ) {
				//LOGGER.debug("该用户尚未登录");
				throw CustomGenericException.CreateException(ResponseCodeEnum.USER_ERROR.getCode(), "该用户尚未登录");
			}
			//验证token
			String token = request.getHeader("accessToken");
			//LOGGER.debug("token: "+token);
			if (null == token || !JwtUil.verify(token)) {
				throw  CustomGenericException.CreateException(ResponseCodeEnum.ERROR.getCode(),"用户的token无效");
			}
			return true;
	}

	//计算访问路径
	private String getPath(HttpServletRequest request) {
		//LOGGER.debug("到达权限拦截器");
		String requestUri = request.getRequestURI();  //得到项目名开始的路径 /test/test.jsp
		if(requestUri.startsWith(request.getContextPath())){ //项目名 /test
			requestUri = requestUri.substring(request.getContextPath().length()); //得到 /test.jsp
		}
		//LOGGER.debug("拦截的路径："+requestUri);
		return requestUri;
	}
}