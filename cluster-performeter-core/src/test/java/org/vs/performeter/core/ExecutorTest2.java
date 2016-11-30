package org.vs.performeter.core;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.vs.performeter.core.CommonConfiguration;

import java.util.concurrent.Executor;

/**
 * Created by dekar on 29.11.2016.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = CommonConfiguration.class, loader = AnnotationConfigContextLoader.class)
@TestPropertySource( locations = {"classpath:app.properties"} )
public class ExecutorTest2 {
    @Autowired
    Executor executor;

    @Test
    public void beanExistTest() throws Exception {
        Assert.assertNotNull(executor);

    }
}
