package com.ireadygo.springbootsxt.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import java.util.Map;

@Controller
public class LoginController {

    @PostMapping("/user/login")
    public String login(@RequestParam("username") String userName,
                        @RequestParam("password") String password,
                        Map<String, Object> map, HttpSession httpSession) {
        if (!StringUtils.isEmpty(userName) && "123456".equals(password)) {
            // 登录成功，防止表单重复提交，可以重定向到主页
            httpSession.setAttribute("loginUser", userName);
            return "redirect:/main.html";
        } else {
            // 登录失败
            map.put("msg", "用户名密码错误");
            return "login";
        }
    }
}
