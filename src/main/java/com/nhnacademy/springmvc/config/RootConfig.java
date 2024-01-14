package com.nhnacademy.springmvc.config;

import com.nhnacademy.springmvc.Base;
import com.nhnacademy.springmvc.domain.Inquiry;
import com.nhnacademy.springmvc.domain.User;
import com.nhnacademy.springmvc.repository.InquiryRepository;
import com.nhnacademy.springmvc.repository.InquiryRepositoryImpl;
import com.nhnacademy.springmvc.repository.UserRepository;
import com.nhnacademy.springmvc.repository.UserRepositoryImpl;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.stereotype.Controller;

@ComponentScan(basePackageClasses = Base.class,
        excludeFilters = { @ComponentScan.Filter(Controller.class)})
public class RootConfig {
    @Bean
    public MessageSource messageSource() {
        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        messageSource.setDefaultEncoding("UTF-8");
        messageSource.setBasename("message");

        return messageSource;
    }

    @Bean
    public UserRepository userRepository(){
        UserRepository userRepository = new UserRepositoryImpl();
        userRepository.addUser(
                User.create("admin_main", "1234")
                        .setName("이진우")
                        .setAuth(User.Auth.ROLE_ADMIN));

        userRepository.addUser(
                User.create("admin_sub", "1234")
                        .setName("김진우")
                        .setAuth(User.Auth.ROLE_ADMIN));

        userRepository.addUser(
                User.create("jinwoo", "4567")
                        .setName("박진우")
                        .setAuth(User.Auth.ROLE_CUSTOMER));

        userRepository.addUser(
                User.create("hahajinwoo", "haha12")
                        .setName("최진우")
                        .setAuth(User.Auth.ROLE_CUSTOMER));

        return userRepository;
    }

    @Bean
    public InquiryRepository inquiryRepository(){
        InquiryRepository inquiryRepository = new InquiryRepositoryImpl();
        inquiryRepository.registerInquiry(
                Inquiry.create("jinwoo")
                        .setType(Inquiry.Type.TYPE_COMPLIMENT)
                        .setTitle("So good")
                        .setComment("just so good"));

        inquiryRepository.registerInquiry(
                Inquiry.create("jinwoo")
                        .setType(Inquiry.Type.TYPE_COMPLIMENT)
                        .setTitle("So good!!")
                        .setComment("just so good!!!"));

        inquiryRepository.registerInquiry(
                Inquiry.create("jinwoo")
                        .setType(Inquiry.Type.TYPE_SUGGEST)
                        .setTitle("Suggest")
                        .setComment("just Suggest"));

        inquiryRepository.registerInquiry(
                Inquiry.create("jinwoo")
                        .setType(Inquiry.Type.TYPE_EXCHANGE_REFUND)
                        .setTitle("Nope")
                        .setComment("Nope!!!"));

        inquiryRepository.registerInquiry(
                Inquiry.create("jinwoo")
                        .setType(Inquiry.Type.TYPE_OTHER)
                        .setTitle("Guitar")
                        .setComment("just so good"));

        inquiryRepository.registerInquiry(
                Inquiry.create("hahajinwoo")
                        .setType(Inquiry.Type.TYPE_COMPLIMENT)
                        .setTitle("So bad")
                        .setComment("just so bad"));

        return inquiryRepository;
    }
}
