package com.tansun.exam.controller;

import com.tansun.exam.model.Employee;
import com.tansun.exam.service.DuanJiaChiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


/**
 * 这是个演示控制层，大家根据自己姓名分别命名，以防止代码冲突
 */
@RestController
public class DuanJiaChiController {

    @Autowired
    private DuanJiaChiService duanJiaChiService;

    @RequestMapping(method = RequestMethod.GET, value = "/myTest")
    public List<Employee> myTest(int pageNum,int pageSize){
        //演示方法
        List<Employee> employeeList = duanJiaChiService.myTest((pageNum-1)*pageSize,pageSize);
        for (Employee employee: employeeList) {
            System.out.println(employee.getName() + "您好！欢迎光临");
        }
        return employeeList;
    }
}
