package com.nhnacademy.springmvc.controller;

import com.nhnacademy.springmvc.domain.User;
import com.nhnacademy.springmvc.exception.UnAuthorizedException;
import com.nhnacademy.springmvc.exception.UserNotFoundException;
import com.nhnacademy.springmvc.repository.UserRepository;
import java.util.Objects;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

@Slf4j
@Controller
public class LoginController {
    private final UserRepository userRepository;

    public LoginController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/login")
    public String login(HttpServletRequest request) {
        HttpSession session = request.getSession();

        return Objects.isNull(session.getAttribute("login")) ? "thymeleaf/login" :  "redirect:/";
    }

    @PostMapping("/login")
    public String doLogin(@RequestParam("id") String id,
                          @RequestParam("password") String password,
                          HttpServletRequest request) {
        if(!userRepository.matches(id, password)){
            throw new UnAuthorizedException();
        }

        HttpSession session = request.getSession();
        User loginUser = userRepository.getUser(id);
        session.setAttribute("login", loginUser);

        return "redirect:/";

    }

    @PostMapping("/logout")
    public String logOut(HttpServletRequest request){
        HttpSession session = request.getSession(false);
        session.removeAttribute("login");

        return "redirect:/login";
    }

    @ExceptionHandler(UserNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ModelAndView notFoundException(UserNotFoundException exception) {
        String errorMsg = "Not Found User";
        log.error(errorMsg, exception);
        ModelAndView mav = new ModelAndView("thymeleaf/login");
        mav.addObject("errorMsg", errorMsg);

        return mav;
    }

    @ExceptionHandler(UnAuthorizedException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ModelAndView unAuthorizedException(UnAuthorizedException exception){
        String errorMsg = "Not match password";
        log.error(errorMsg, exception);
        ModelAndView mav = new ModelAndView("thymeleaf/login");
        mav.addObject("errorMsg", errorMsg);

        return mav;
    }
}
