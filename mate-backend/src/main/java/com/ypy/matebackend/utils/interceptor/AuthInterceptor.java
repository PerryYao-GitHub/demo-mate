package com.ypy.matebackend.utils.interceptor;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;


@Component
public class AuthInterceptor implements HandlerInterceptor {
    /*
    * 请求体中的 Authorization: "Bearer " + this.store.user.token
    * JWT version
    * */
//    @Override
//    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
//        // 允许所有 OPTIONS 请求通过  !!!!!!!!!! 不加以下内容会影响跨域
//        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) return true;
//
//        String authorizationHeader = request.getHeader("Authorization");
//
//        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
//            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//            response.getWriter().write("Missing or invalid Authorization header");
//            return false;
//        }
//
//        String token = authorizationHeader.substring(7);
//        Map<String, Object> claims;
//        try {
//            claims = JwtUtils.parseToken(token);  // 这一过程包含了对token合法性检验
//        } catch (Exception e) {
//            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//            response.getWriter().write("Invalid token");
//            return false;
//        }
//
//        request.setAttribute("claims", claims);
//        return true;
//    }

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
