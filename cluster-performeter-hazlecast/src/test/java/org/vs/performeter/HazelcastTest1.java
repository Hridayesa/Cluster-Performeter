package org.vs.performeter;

import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.assertEquals;

/**
 * Created by karpovdc on 01.09.2015.
 */
public class HazelcastTest1 {
    @Test
    public void testName() throws Exception {
        HazelcastInstance hz = Hazelcast.newHazelcastInstance();
        Map<String, String> map = hz.getMap("map");
        map.put("1", "Tokyo");
        map.put("2", "Paris");
        map.put("3", "New York");
        System.out.println("Finished loading map");

        HazelcastInstance hz1 = Hazelcast.newHazelcastInstance();
        Map<String, String> map1 = hz.getMap("map");
        for (Map.Entry<String, String> entry : map.entrySet()) {
            assertEquals(entry.getValue(), map1.get(entry.getKey()));
        }
    }
}
