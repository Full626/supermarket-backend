package com.config;

import com.annotation.RoleRequired;
import com.domain.User;
import com.mapper.UserMapper;
import com.util.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class RoleAuthInterceptor implements HandlerInterceptor {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserMapper userMapper;  // 注入 UserMapper

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }

        HandlerMethod handlerMethod = (HandlerMethod) handler;
        RoleRequired roleRequired = handlerMethod.getMethodAnnotation(RoleRequired.class);

        if (roleRequired == null) {
            return true;
        }

        String userId = (String) request.getAttribute("userId");
        if (userId == null) {
            sendErrorResponse(response, "无法获取用户信息");
            return false;
        }

        // 从数据库获取用户角色
        User user = userMapper.findByUserId(userId);
        if (user == null) {
            sendErrorResponse(response, "用户不存在");
            return false;
        }
        String role = user.getRole();

        String[] allowedRoles = roleRequired.value();
        for (String allowedRole : allowedRoles) {
            if (allowedRole.equals(role)) {
                return true;
            }
        }

        sendErrorResponse(response, "权限不足，需要角色：" + String.join(",", allowedRoles));
        return false;
    }

    private void sendErrorResponse(HttpServletResponse response, String message) throws Exception {
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setContentType("application/json;charset=UTF-8");
        com.common.Result<Void> result = com.common.Result.error(403, message);
        com.fasterxml.jackson.databind.ObjectMapper mapper = new com.fasterxml.jackson.databind.ObjectMapper();
        response.getWriter().write(mapper.writeValueAsString(result));
    }
}