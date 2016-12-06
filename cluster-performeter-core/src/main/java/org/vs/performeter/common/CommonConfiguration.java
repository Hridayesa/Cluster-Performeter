package org.vs.performeter.common;

import com.hazelcast.core.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * Created by dekar on 29.11.2016.
 */
@Configuration
@ComponentScan
public class CommonConfiguration {

    @Bean
    public HazelcastInstance hazelcast(){
        return Hazelcast.newHazelcastInstance();
    }

    @Bean
    public ITopic controlTopic(){
        return hazelcast().getTopic("controlTopic");
    }

    @Bean
    public IMap context(){
        return hazelcast().getMap("context");
    }

    @Bean
    public IMap statisticsMap(){
        return hazelcast().getMap("statisticsMap");
    }

    @Bean
    public ICountDownLatch finishCollectionLatch(){
        return hazelcast().getCountDownLatch("finishCollectionLatch");
    }


    @Bean
    public Executor taskExecutor(@Value("${executer.corePoolSize}") Integer corePoolSize,
                                 @Value("${executer.maxPoolSize}") Integer maxPoolSize,
                                 @Value("${executer.queueCapacity}") Integer queueCapacity) {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(corePoolSize);
        executor.setMaxPoolSize(maxPoolSize);
        executor.setQueueCapacity(queueCapacity);
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        executor.initialize();
        return executor;
    }

}
