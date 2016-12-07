//package org.vs.prototype;
//
//import com.hazelcast.core.HazelcastInstance;
//import com.hazelcast.core.IMap;
//import org.junit.Assert;
//import org.junit.Test;
//import org.junit.orchestrator.RunWith;
//import org.springframework.test.context.ContextConfiguration;
//import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
//import TestContainer;
//
//import javax.annotation.Resource;
//
///**
// * Created by Denis Karpov on 07.09.2015.
// */
//@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration(locations = { "/core/parent.cfg.xml",
//        "/core/tester.cfg.xml","/hazelcast-test/hazelcast.cfg.xml" })
//public class SpringTest1 {
//    @Resource private TestContainer testContainer;
//    @Resource private HazelcastInstance hazelcastInstance;
//    @Resource private IMap testMap;
//
//    @Test
//    public void testHzInst() throws Exception {
//        Assert.assertNotNull(hazelcastInstance);
//    }
//
//    @Test
//    public void testMap1() throws Exception {
//        Assert.assertNotNull(testMap);
//        testMap.put("qwerty", 123456);
//        Assert.assertEquals(123456, testMap.get("qwerty"));
//    }
//
//    @Test
//    public void testName() throws Exception {
//        Assert.assertNotNull(testContainer);
//    }
//}
