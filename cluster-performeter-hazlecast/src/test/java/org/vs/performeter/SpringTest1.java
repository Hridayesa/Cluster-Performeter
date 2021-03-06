package org.vs.performeter;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTestContextBootstrapper;
import org.springframework.test.context.BootstrapWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.vs.performeter.analyser.Orchestrator;
import org.vs.performeter.common.CommonConfiguration;
import org.vs.performeter.tester.TestContainer;
import org.vs.performeter.tester.Tester;
import org.vs.performeter.tester.TesterMain;

import javax.annotation.Resource;

/**
 * Created by Denis Karpov on 07.09.2015.
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {CommonConfiguration.class, TesterMain.class})
@BootstrapWith(SpringBootTestContextBootstrapper.class)
public class SpringTest1 {
    @Autowired private Tester tester;
    @Resource private TestContainer testContainer;
    @Autowired(required = false) private Orchestrator orchestrator;
    @Resource private HazelcastInstance hazelcastInstance;
    @Resource private IMap testMap;

    @Test
    public void testHzInst() throws Exception {
        Assert.assertNotNull(hazelcastInstance);
    }

    @Test
    public void testMap1() throws Exception {
        Assert.assertNotNull(testMap);
        testMap.put("qwerty", 123456);
        Assert.assertEquals(123456, testMap.get("qwerty"));
    }

    @Test
    public void testContainerExistence() throws Exception {
        Assert.assertNotNull(testContainer);
    }

    @Test
    public void testRunnerExistence() throws Exception {
        Assert.assertNull(orchestrator);
    }

    @Test
    public void testTesterExistence() throws Exception {
        Assert.assertNotNull(tester);
    }
}
