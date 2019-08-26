package com.ireadygo.springbootsxt.controller;

import com.ireadygo.springbootsxt.dao.EmployeeDao;
import com.ireadygo.springbootsxt.entities.Employee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Collection;

@Controller
public class EmployeeController {

    @Autowired
    public EmployeeDao employeeDao;

    // 查询所有员工返回列表页面
    @GetMapping("/emps")
    public String list(Model model){
        Collection<Employee> employees =  employeeDao.getAll();
        model.addAttribute("emps",employees);

        // thymeleaf默认会拼接字符串
        // classpath:/templates/xx.html
        return "emp/list";
    }
}
