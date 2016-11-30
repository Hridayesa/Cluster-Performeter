package org.vs.performeter.core;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.vs.performeter.core.CommonConfiguration;

/**
 * Created by dekar on 29.11.2016.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {CommonConfiguration.class}, loader = AnnotationConfigContextLoader.class)
@TestPropertySource( locations = {"classpath:app.properties"} )
//@PropertySource("classpath:app.properties")
public class PropertiesTest {
    @Autowired
    ApplicationContext context;
    @Value("${executer.corePoolSize}") Integer corePoolSize;

    @Test
    public void propExistsTest() throws Exception {
        System.out.printf("qqqq %s", context);
        Assert.assertEquals(new Integer(20), corePoolSize);

    }
}
