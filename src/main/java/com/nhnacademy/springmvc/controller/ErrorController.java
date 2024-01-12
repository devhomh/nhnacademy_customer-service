package com.nhnacademy.springmvc.controller;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/error")
public class ErrorController {
    @GetMapping("/401")
    public ModelAndView unAuthorizedPage(){

        ModelAndView mav = new ModelAndView("thymeleaf/error");
        mav.setStatus(HttpStatus.UNAUTHORIZED);
        mav.addObject("httpStatusCode", 401);
        mav.addObject("errorMsg", "Sorry Not Authorize");

        return mav;
    }
}
