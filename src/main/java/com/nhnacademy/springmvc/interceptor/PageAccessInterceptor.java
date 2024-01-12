package com.nhnacademy.springmvc.interceptor;

import com.nhnacademy.springmvc.domain.User;
import java.util.Objects;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.springframework.web.servlet.HandlerInterceptor;

public class PageAccessInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("login");
        String reqPath = request.getRequestURI();

        if(!reqPath.contains(user.getId())){
            response.sendRedirect("/error/401");

            return false;
        }

        return true;
    }
}
