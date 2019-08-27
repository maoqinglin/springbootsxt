package com.ireadygo.springbootsxt.controller;

import com.ireadygo.springbootsxt.dao.DepartmentDao;
import com.ireadygo.springbootsxt.dao.EmployeeDao;
import com.ireadygo.springbootsxt.entities.Department;
import com.ireadygo.springbootsxt.entities.Employee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.Collections;

@Controller
public class EmployeeController {

    @Autowired
    public EmployeeDao employeeDao;

    @Autowired
    public DepartmentDao departmentDao;

    // 查询所有员工返回列表页面
    @GetMapping("/emps")
    public String list(Model model) {
        Collection<Employee> employees = employeeDao.getAll();
        model.addAttribute("emps", employees);

        // thymeleaf默认会拼接字符串
        // classpath:/templates/xx.html
        return "emp/list";
    }

    // 来到员工添加页面
    @GetMapping("/emp")
    public String toAddPage(Model model) {
        // 来到添加页面，查出所有的部门，在页面显示
        Collection<Department> departments = departmentDao.getDepartments();
        model.addAttribute("depts", departments);
        return "emp/add";
    }

    // 员工添加
    // SpringMVC自动将请求参数与入参对象的属性进行--绑定，要求请求参数的名字和javabean入参对象里面的属性名一致
    @PostMapping("/emp")
    public String addEmp(Employee employee) {
        // 来到员工列表页面
        employeeDao.save(employee);
        return "redirect:/emps";
    }

    // 来到修改页面，查询当前员工，在页面回显
    @GetMapping("/emp/{id}")
    public String toEditPage(@PathVariable("id") Integer id, Model model) {
        Employee employee = employeeDao.get(id);
        // 页面显示所有的部门列表
        Collection<Department> departments = departmentDao.getDepartments();
        model.addAttribute("emp", employee);
        model.addAttribute("depts", departments);

        // 回到修改页面（add是一个修改添加二合一的页面）
        return "emp/add";
    }

    // 员工修改，需要提交员工的id
    @PutMapping("/emp")
    public String updateEmployee(Employee employee) {
        employeeDao.save(employee);
        return "redirect:/emps";
    }

    // 删除员工
    @DeleteMapping("/emp/{id}")
    public String deleteEmployee(@PathVariable("id") Integer id) {
        employeeDao.delete(id);
        return "redirect:/emps";
    }
}
