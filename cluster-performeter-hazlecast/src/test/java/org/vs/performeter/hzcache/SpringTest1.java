package org.vs.performeter.hzcache;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.vs.performeter.core.CommonConfiguration;
import org.vs.performeter.core.common.Tester;
import org.vs.performeter.core.container.Orchestrator;
import org.vs.performeter.core.container.TestContainer;
import org.vs.performeter.hzcache.tester.TesterMain;

import javax.annotation.Resource;

/**
 * Created by karpovdc on 07.09.2015.
 */

//@ContextConfiguration(locations = { "/core/parent.cfg.xml",
//        "/core/tester.cfg.xml","/hazelcast-test/hazelcast.cfg.xml" })
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {CommonConfiguration.class, TesterMain.class}, loader = AnnotationConfigContextLoader.class)
@TestPropertySource( locations = {"classpath:app.properties"}, properties = {"isTesterInstance = true"})
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
