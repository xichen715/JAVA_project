package com.xichen.tourism.utils;

import com.xichen.tourism.constant.Constants;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

public class TokenUtils {

    /**
    * 解析token获取userId
    */
    public static String getUserIdByToken() {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = requestAttributes.getRequest();
        String token = request.getHeader(Constants.X_ACCESS_TOKEN);
        return JwtUtil.getUserId(token);
    }

}
