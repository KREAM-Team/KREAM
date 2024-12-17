package com.kream.kream.controllers;

import com.kream.kream.entitys.UserEntity;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.servlet.ModelAndView;

//@Controller
//@RequestMapping(value = "/")
//public class HomeController {
//
//    @RequestMapping(value = "/", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
//
//    public ModelAndView getIndex(@SessionAttribute(value = "user", required = false) UserEntity user) {
//        ModelAndView modelAndView = new ModelAndView();
//        if (user == null) {
//            // 세션에 "user" 속성이 없으면 (로그인하지 않은 사용자)
//            modelAndView.setViewName("user/login");
//            // "home/index.unsigned"라는 뷰를 설정. 이 뷰는 비로그인 상태의 메인 화면을 렌더링.
//        } else {
//            // 세션에 "user" 속성이 있으면 (로그인한 사용자)
//            modelAndView.setViewName("my");
//            // "home/index.signed"라는 뷰를 설정. 이 뷰는 로그인한 상태의 메인 화면을 렌더링.
//        }
//        return modelAndView;
//    }
//}
