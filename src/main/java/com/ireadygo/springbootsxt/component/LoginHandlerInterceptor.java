package com.ireadygo.springbootsxt.component;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class LoginHandlerInterceptor implements HandlerInterceptor {
    Logger logger = LoggerFactory.getLogger(getClass());
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws ServletException, IOException {
        Object user = request.getSession().getAttribute("loginUser");
        if (user == null) {
            // 未登录，返回登录页面
            request.setAttribute("msg", "没有权限请先登录");
            request.getRequestDispatcher("/index.html").forward(request, response);
            return false;
        } else {
            // 已登录，放行请求
            return true;
        }
    }
}
