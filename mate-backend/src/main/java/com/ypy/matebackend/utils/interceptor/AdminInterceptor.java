package com.ypy.matebackend.utils.interceptor;

import com.ypy.matebackend.common.UserRole;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@Component
public class AdminInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 允许所有 OPTIONS 请求通过
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) return true;

        // 从Session中获取用户信息
        Map<String, Object> user = (Map<String, Object>) request.getSession().getAttribute("user");
        if (user == null || !UserRole.ADMIN.getCode().equals(user.get("role"))) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.getWriter().write("You do not have permission to access this resource.");
            return false;
        }
        return true;
    }
}
