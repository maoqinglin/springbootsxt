package com.ireadygo.springbootsxt;

import com.ireadygo.springbootsxt.bean.Person;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit4.SpringRunner;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SpringbootsxtApplicationTests {

    // 记录器
    Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    Person person;

    @Autowired
    ApplicationContext ioc;

    @Autowired
    DataSource dataSource;

    @Test
    public void testHelloService() {
        boolean existBean = ioc.containsBean("helloService");
        logger.debug("existBean = " + existBean);
    }


    @Test
    public void contextLoads() {
       logger.debug(String.valueOf(person));
    }


    @Test
    public void testDataSource() throws SQLException {
        logger.info("dataSource: "+dataSource.getClass());
        Connection connection = dataSource.getConnection();
        logger.info(connection.toString());
        connection.close();
    }

}
