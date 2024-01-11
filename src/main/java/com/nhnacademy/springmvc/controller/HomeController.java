package com.nhnacademy.springmvc.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class HomeController {
    //TODO: Session이 있으면 Auth에 맞는 유저 페이지로 이동 없으면 로그인 폼으로 이동
    @GetMapping
    public String home(){
        return "thymeleaf/index";
    }
}
