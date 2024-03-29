package com.ireadygo.springbootsxt.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Controller
public class HelloController {

    @Autowired
    JdbcTemplate jdbcTemplate;

    @GetMapping("/hello")
    @ResponseBody
    public String hello() {
        return "hello world!";
    }

    @GetMapping("/success")
    public String success(Map<String, Object> maps) {
        maps.put("hello", "<h1>你好</h1>");
        maps.put("users", Arrays.asList("zhangsan", "lisi", "wangwu"));
        return "success";
    }

    @ResponseBody
    @GetMapping("/query")
    public List<Map<String, Object>> map() {
        List<Map<String, Object>> depts = jdbcTemplate.queryForList("select * from department");
        return depts;
    }
}
