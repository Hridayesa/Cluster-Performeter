package org.vs.performeter;

import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTestContextBootstrapper;
import org.springframework.test.context.BootstrapWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.vs.performeter.common.CommonConfiguration;

import javax.annotation.Resource;
import java.util.Map;

import static org.junit.Assert.assertEquals;

/**
 * Created by Denis Karpov on 01.09.2015.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = CommonConfiguration.class)
@BootstrapWith(SpringBootTestContextBootstrapper.class)
public class HazelcastTest1 {
    @Resource
    IMap context;

    @Test
    public void testName() throws Exception {

        context.put("1", "Tokyo");
        context.put("2", "Paris");
        context.put("3", "New York");
        System.out.println("Finished loading map");

        HazelcastInstance hz1 = Hazelcast.newHazelcastInstance();
        Map<String, String> map1 = hz1.getMap("context");
        Map<String, String> map = context;
        for (Map.Entry entry : map.entrySet()) {
            assertEquals(entry.getValue(), map1.get(entry.getKey()));
        }
    }
}
