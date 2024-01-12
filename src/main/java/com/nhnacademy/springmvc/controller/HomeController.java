package com.nhnacademy.springmvc.controller;

import com.nhnacademy.springmvc.domain.User;
import java.util.Objects;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class HomeController {
    @GetMapping
    public String home(HttpServletRequest request){
        HttpSession session = request.getSession();
        User loginUser = (User) session.getAttribute("login");
        if(Objects.isNull(loginUser)){
            return "thymeleaf/login";
        }

        return loginUser.getAuth() == User.Auth.ROLE_ADMIN
                ? "redirect:/admin/" + loginUser.getId()
                : "redirect:/user/" + loginUser.getId();
    }
}
