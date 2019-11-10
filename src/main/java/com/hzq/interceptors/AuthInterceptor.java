package com.hzq.interceptors;

import com.hzq.common.Const;
import com.hzq.enums.ResponseCodeEnum;
import com.hzq.execption.CustomGenericException;
import com.hzq.domain.User;
import com.hzq.handler.ChatWebSocketHandler;
import com.hzq.service.UserService;
import com.hzq.utils.JwtUil;
import com.hzq.vo.SendMessage;
import com.hzq.vo.ServerResponse;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import org.springframework.web.socket.WebSocketSession;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.concurrent.ConcurrentHashMap;

public class AuthInterceptor extends HandlerInterceptorAdapter {

	private static final Logger LOGGER = LoggerFactory.getLogger(AuthInterceptor.class);
	@Autowired
	private UserService userService;
	@Autowired
	private ChatWebSocketHandler chat;

	@Override
		public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
			//查看请求路径
			String requestUri = getPath(request);
			//系统根目录,直接放行。
			if (StringUtils.equals("/",requestUri)) {
				return true;
			}
			String username = request.getHeader(Const.USERNAME);
			LOGGER.debug("用户名："+username);
			if(username != null && ("//user/select/"+username).equals(requestUri)) { //忘记密码要查询
				return true;
			}
			if ("//user/update".equals(requestUri)) { //通过密保之后，修改密码可以放行
				return true;
			}
			//判断是否已经登录，强制退出
			if ("//user/login".equals(requestUri)) {
				LOGGER.debug("用户名："+username);
				ServerResponse<User> resp = userService.selectByUsername(username);
				if (resp.isSuccess()) {
					Integer id = resp.getData().getId();
					ConcurrentHashMap<Integer, WebSocketSession> map = chat.getUserSessionMap();
					if (map.isEmpty() || map.get(id) == null) {
						return true;
					} else {
						SendMessage message = new SendMessage(Const.WITHDRAW,null);
						chat.sendMessageToUser(id,message);
						return true;
					}
				}else {
					throw CustomGenericException.CreateException(ResponseCodeEnum.USER_ERROR.getCode(),"用户不存在");
				}
			}

			//其他需要登录后才能进行访问的url
			User user = (User) request.getSession().getAttribute(Const.CURRENT_USER);
			if (null == user ) {
				LOGGER.debug("该用户尚未登录");
				throw CustomGenericException.CreateException(ResponseCodeEnum.USER_ERROR.getCode(), "该用户尚未登录");
			}
			//验证token
			String token = request.getHeader("accessToken");
			if (null == token || !JwtUil.verify(token)) {
				throw  CustomGenericException.CreateException(ResponseCodeEnum.ERROR.getCode(),"用户的token无效");
			}
			return true;
	}

	//计算访问路径
	private String getPath(HttpServletRequest request) {
		String requestUri = request.getRequestURI();  //得到项目名开始的路径 /test/test.jsp
		if(requestUri.startsWith(request.getContextPath())){ //项目名 /test
			String url = requestUri.substring(request.getContextPath().length()); //得到 /test.jsp
			LOGGER.debug("访问的路径---"+url);
			return url;
		}
		return null;
	}
}