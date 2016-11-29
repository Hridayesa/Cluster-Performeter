package org.vs.performeter;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.scheduling.SchedulingTaskExecutor;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.util.stream.IntStream;

/**
 * Created by karpovdc on 11.09.2015.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/tmp.xml" })
public class ExecutorTest {
    @Resource private SchedulingTaskExecutor taskExecutor;
    @Test
    public void testName1() throws Exception {
        IntStream.rangeClosed(1, 1000)
                .forEach(value -> {
                            taskExecutor.execute(() -> {
                                try {
                                    Thread.sleep(100);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                System.out.println(value);
                            });
                        }
                );
        Assert.assertEquals(1, 1);
    }
}
