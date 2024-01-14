package com.nhnacademy.springmvc.controller;

import com.nhnacademy.springmvc.domain.Inquiry;
import com.nhnacademy.springmvc.domain.User;
import com.nhnacademy.springmvc.repository.InquiryRepository;
import com.nhnacademy.springmvc.repository.UserRepository;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/user")
public class UserController {
    private final UserRepository userRepository;
    private final InquiryRepository inquiryRepository;

    public UserController(UserRepository userRepository, InquiryRepository inquiryRepository) {
        this.userRepository = userRepository;
        this.inquiryRepository = inquiryRepository;
    }

    private List<String> getTypeEnumList(){
        return EnumSet.allOf(Inquiry.Type.class)
                .stream()
                .map(Inquiry.Type::value)
                .collect(Collectors.toList());
    }


    @GetMapping(value = "/{userId}")
    public String userPage(@PathVariable("userId") String id,
                           @RequestParam(value = "type", defaultValue = "칭찬") String type,
                           ModelMap modelMap){
        User user = userRepository.getUser(id);
        modelMap.addAttribute("user", user);

        List<Inquiry> inquiryList = inquiryRepository
                .findAll()
                .keySet()
                .stream()
                .map(inquiryRepository::getInquiry)
                .filter(inquiry -> Objects.equals(inquiry.getUserId(), id))
                .filter(inquiry -> Objects.equals(inquiry.getType().value(), type))
                .collect(Collectors.toList());

        Collections.reverse(inquiryList);
        modelMap.addAttribute("selectedType", type);
        modelMap.addAttribute("typeList", getTypeEnumList());
        modelMap.addAttribute("inquiryList", inquiryList);

        return "thymeleaf/userPage";
    }
}
