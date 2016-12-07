package org.vs.performeter;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTestContextBootstrapper;
import org.springframework.test.context.BootstrapWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.vs.performeter.common.CommonConfiguration;

import javax.annotation.Resource;
import java.util.concurrent.Executor;

/**
 * Created by Denis Karpov on 29.11.2016.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = CommonConfiguration.class)
@BootstrapWith(SpringBootTestContextBootstrapper.class)
public class ExecutorTest2 {
    @Resource
    Executor taskExecutor;

    @Test
    public void beanExistTest() throws Exception {
        Assert.assertNotNull(taskExecutor);

    }
}
