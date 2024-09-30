package com.ypy.matebackend.utils.interceptor;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;


@Component
public class AuthInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 允许所有 OPTIONS 请求通过，跨域处理
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) return true;

        // 检查Session中是否有用户信息
        Object user = request.getSession().getAttribute("user");
        if (user == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("User not logged in or session expired");
            return false;
        }

        // 将用户信息传递到后续处理
        request.setAttribute("user", user);
        return true;
    }
}
