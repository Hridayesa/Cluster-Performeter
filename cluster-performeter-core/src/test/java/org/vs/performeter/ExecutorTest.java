package org.vs.performeter;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTestContextBootstrapper;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.test.context.BootstrapWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.vs.performeter.common.CommonConfiguration;

import javax.annotation.Resource;
import java.util.concurrent.Executor;
import java.util.stream.IntStream;

/**
 * Created by Denis Karpov on 11.09.2015.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = CommonConfiguration.class)
@BootstrapWith(SpringBootTestContextBootstrapper.class)
public class ExecutorTest {
    @Resource
    private Executor taskExecutor;

    @Test
    public void testExecutorConfig() throws Exception {
        ThreadPoolTaskExecutor exec = (ThreadPoolTaskExecutor) taskExecutor;
        Assert.assertEquals(20, exec.getCorePoolSize());
        Assert.assertEquals(40, exec.getMaxPoolSize());
    }

    @Test
    public void testName1() throws Exception {
        IntStream.rangeClosed(1, 100)
                .forEach(value -> taskExecutor.execute(() -> {
                            try {
                                Thread.sleep(100);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            System.out.println(value);
                        })
                );
        Assert.assertEquals(1, 1);
    }
}
