package com.nhnacademy.springmvc.interceptor;

import com.nhnacademy.springmvc.domain.User;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;

@Slf4j
public class PageAccessInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("login");
        String reqPath = request.getRequestURI();
        String uriUser = reqPath.substring(reqPath.lastIndexOf("/") + 1);

        if(!uriUser.equals(user.getId())){
            response.sendRedirect("/error/401");

            return false;
        }

        return true;
    }
}
