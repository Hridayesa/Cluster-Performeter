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
import org.vs.performeter.common.CommonConfiguration;
import org.vs.performeter.common.IgniteTesterConfiguration;
import org.vs.performeter.data.dummy.Msg;

import javax.annotation.Resource;

/**
 * Created by Denis Karpov on 02.12.2016.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {IgniteTesterConfiguration.class,
        CommonConfiguration.class})
@BootstrapWith(SpringBootTestContextBootstrapper.class)
public class IgniteTest1 {
    @Autowired
    Ignite ignite;

    @Resource(name = "counter")
    IgniteCache<String,Msg> counter;

    @Test
    public void testIgnite1() throws Exception {
        Assert.assertNotNull(ignite);
        Assert.assertNotNull(counter);
    }
}
