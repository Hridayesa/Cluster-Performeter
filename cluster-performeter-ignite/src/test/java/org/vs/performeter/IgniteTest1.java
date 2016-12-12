package org.vs.performeter;

import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTestContextBootstrapper;
import org.springframework.test.context.BootstrapWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.vs.performeter.common.IgniteTesterConfiguration;

import javax.annotation.Resource;
import java.util.concurrent.locks.Lock;

/**
 * Created by Denis Karpov on 02.12.2016.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {IgniteTesterConfiguration.class})
@BootstrapWith(SpringBootTestContextBootstrapper.class)
public class IgniteTest1 {
    @Autowired
    Ignite ignite;

    @Resource(name = "counter")
    IgniteCache<Integer,Integer> counter;

    @Test
    public void testIgnite1() throws Exception {
        Assert.assertNotNull(ignite);
        Assert.assertNotNull(counter);
        counter.put(20, 3);
        Assert.assertEquals(3, counter.get(20).intValue());
    }
    @Test
    public void lockTest() throws Exception {
        Lock lock = counter.lock(5);
        lock.lock();
        try {
            counter.put(5,999);
        } finally {
            lock.unlock();
        }
        Assert.assertEquals(999, counter.get(5).intValue());
    }
}
