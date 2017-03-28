package org.vs.performeter.common;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * Created by Denis Karpov on 06.12.2016.
 */
@Configuration
@ComponentScan({"org.vs.performeter.data.dummy",
        "org.vs.performeter.data.collision"})
public class HazelcastConfiguration {
    @Bean("testMap")
    public IMap testMap(@Autowired HazelcastInstance hazelcast) {
        return hazelcast.getMap("testMap");
    }
}
