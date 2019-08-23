package com.ireadygo.springbootsxt;

import com.ireadygo.springbootsxt.bean.Person;
import com.ireadygo.springbootsxt.service.HelloService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SpringbootsxtApplicationTests {

    @Autowired
    Person person;

    @Autowired
    ApplicationContext ioc;

    @Test
    public void testHelloService() {
        boolean existBean = ioc.containsBean("helloService");
        System.out.println("existBean = " + existBean);
    }


    @Test
    public void contextLoads() {
        System.out.println(person);
    }

}
