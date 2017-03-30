package org.vs.performeter;

import org.infinispan.Cache;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTestContextBootstrapper;
import org.springframework.test.context.BootstrapWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.vs.performeter.common.CommonConfiguration;
import org.vs.performeter.common.InfinispanConfiguration;
import org.vs.performeter.data.dummy.Msg;

import javax.annotation.Resource;

/**
 * Created by Denis Karpov on 01.09.2015.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {InfinispanConfiguration.class, CommonConfiguration.class})
@BootstrapWith(SpringBootTestContextBootstrapper.class)
public class InfinispanTest1 {
    @Resource(name = "testMap")
    private Cache<String, Msg> testMap;

    @Test
    public void testExist() throws Exception {
        Assert.assertNotNull(testMap);
    }

    @Test
    public void testCache() throws Exception {
        testMap.put("qqq",new Msg( "1","2","bbbbbbbb"));
        Assert.assertEquals("2",testMap.get("qqq").getName());
    }
}
