package com.nhnacademy.springmvc.controller;

import com.nhnacademy.springmvc.domain.Inquiry;
import com.nhnacademy.springmvc.domain.User;
import com.nhnacademy.springmvc.repository.InquiryRepository;
import com.nhnacademy.springmvc.repository.UserRepository;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/user")
public class UserController {
    private final UserRepository userRepository;
    private final InquiryRepository inquiryRepository;

    public UserController(UserRepository userRepository, InquiryRepository inquiryRepository) {
        this.userRepository = userRepository;
        this.inquiryRepository = inquiryRepository;
    }


    @GetMapping("/{userId}")
    public ModelAndView userPage(@PathVariable("userId") String id){
        ModelAndView mav = new ModelAndView("thymeleaf/userPage");

        User user = userRepository.getUser(id);
        mav.addObject("user", user);

        List<Inquiry> inquiryList = inquiryRepository
                .findAll()
                .keySet()
                .stream()
                .map(inquiryRepository::getInquiry)
                .filter(inquiry -> Objects.equals(inquiry.getUserId(), id))
                .collect(Collectors.toList());

        Collections.reverse(inquiryList);

        mav.addObject("inquiryList", inquiryList);

        return mav;
    }
}
