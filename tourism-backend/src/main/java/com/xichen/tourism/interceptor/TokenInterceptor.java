package com.xichen.tourism.interceptor;

import com.xichen.tourism.constant.Constants;
import com.xichen.tourism.domain.User;
import com.xichen.tourism.service.UserService;
import com.xichen.tourism.utils.JwtUtil;
import com.xichen.tourism.utils.RedisUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@Component
public class TokenInterceptor implements HandlerInterceptor {

    @Autowired
    private RedisUtils redisUtils;
    @Autowired
    private UserService userService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //是否登录
        boolean isLogin = false;
        // 请求头带上令牌 x_access_token :Bearer token
        String userId = JwtUtil.getUserIdByToken(request);
        String token = redisUtils.get(Constants.PREFIX_USER_TOKEN + userId);
        if (null != token) {
            User user = userService.getById(userId);
            //解析token
            try {
                //校验token有效性
                if (JwtUtil.verify(token, userId, user.getPassword())) {
                    isLogin = true;
                    // 核心：将userId放入request，供后续Controller使用
                    request.setAttribute("userId", userId);
                }
            } catch (Exception e) {
                System.out.println("解码异常为》》》》" + e);
            }
        }
        if (!isLogin) {
            //未登录，则响应信息
            response.setContentType("application/json;charset=UTF-8");
            response.setCharacterEncoding("UTF-8");
            response.setStatus(1011);
            response.getWriter().write("登录已过期或未登录，请重新登录");
        }
        //不放行
        return isLogin;
    }

}