package com.nhnacademy.springmvc.controller;

import static org.mockito.ArgumentMatchers.anyString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.mockito.Mockito.when;

import com.nhnacademy.springmvc.domain.User;
import com.nhnacademy.springmvc.repository.InquiryRepository;
import com.nhnacademy.springmvc.repository.UserRepository;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

class PageControllerTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private InquiryRepository inquiryRepository;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp(){
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders
                .standaloneSetup(new PageController(userRepository, inquiryRepository))
                .build();
    }

    @Test
    @DisplayName("로그인 하지 않았을 때 login view로 이동")
    void notLoginHomeTest() throws Exception{
        MockHttpSession session = new MockHttpSession();

        mockMvc.perform(get("/").session(session))
                .andExpect(view().name("thymeleaf/login"));
    }

    @Test
    @DisplayName("관리자가 로그인 했을 때 home 으로 get 매핑")
    void adminLoginHomeTest() throws Exception{
        User user = User.create("tester", "1234")
                .setName("tester")
                .setAuth(User.Auth.ROLE_ADMIN);

        MockHttpSession session = new MockHttpSession();
        session.setAttribute("login", user);

        mockMvc.perform(get("/").session(session))
                .andExpect(redirectedUrl("/admin/" + user.getId()));
    }

    @Test
    @DisplayName("고객이 로그인 했을 때 home 으로 get 매핑")
    void customerLoginHomeTest() throws Exception{
        User user = User.create("tester", "1234")
                .setName("tester")
                .setAuth(User.Auth.ROLE_CUSTOMER);

        MockHttpSession session = new MockHttpSession();
        session.setAttribute("login", user);

        mockMvc.perform(get("/").session(session))
                .andExpect(redirectedUrl("/user/" + user.getId()));
    }

    @Test
    void adminPageTest() throws Exception{
        User user = User.create("tester", "1234")
                .setName("tester")
                .setAuth(User.Auth.ROLE_ADMIN);

        when(userRepository.getUser(anyString())).thenReturn(user);

         mockMvc.perform(get("/admin/{userId}", user.getId()).param("type","칭찬"))
                 .andExpect(view().name("thymeleaf/adminPage"))
                 .andExpect(model().attribute("user", Matchers.hasProperty("id", Matchers.is(user.getId()))))
                 .andExpect(model().attribute("selectedType", Matchers.is("칭찬")))
                 .andExpect(model().attributeExists("typeList"))
                 .andExpect(model().attributeExists("inquiryList"));
    }

    @Test
    void userPage() throws Exception{
        User user = User.create("tester", "1234")
                .setName("tester")
                .setAuth(User.Auth.ROLE_CUSTOMER);

        when(userRepository.getUser(anyString())).thenReturn(user);

        mockMvc.perform(get("/user/{userId}", user.getId()).param("type","칭찬"))
                .andExpect(view().name("thymeleaf/userPage"))
                .andExpect(model().attribute("user", Matchers.hasProperty("id", Matchers.is(user.getId()))))
                .andExpect(model().attribute("selectedType", Matchers.is("칭찬")))
                .andExpect(model().attributeExists("typeList"))
                .andExpect(model().attributeExists("inquiryList"));
    }

}