package org.vs.performeter.common;

import com.hazelcast.core.*;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * Created by Denis Karpov on 29.11.2016.
 */
@Configuration
@ComponentScan
@EnableAutoConfiguration
public class CommonConfiguration {

    @Bean
    public HazelcastInstance hazelcast() {
        return Hazelcast.newHazelcastInstance();
    }

    @Bean
    public ITopic controlTopic() {
        return hazelcast().getTopic("controlTopic");
    }

    @Bean
    public IMap context() {
        return hazelcast().getMap("context");
    }

    @Bean
    public IMap statisticsMap() {
        return hazelcast().getMap("statisticsMap");
    }

    @Bean
    public ICountDownLatch finishCollectionLatch() {
        return hazelcast().getCountDownLatch("finishCollectionLatch");
    }

    @ConfigurationProperties(prefix = "performeter.executor")
    @Bean(initMethod = "initialize")
    public Executor taskExecutor() {
//    public Executor taskExecutor(@Value("${performeter.executor.corePoolSize}") Integer corePoolSize,
//                                 @Value("${performeter.executor.maxPoolSize}") Integer maxPoolSize,
//                                 @Value("${performeter.executor.queueCapacity}") Integer queueCapacity) {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
//        executor.setCorePoolSize(corePoolSize);
//        executor.setMaxPoolSize(maxPoolSize);
//        executor.setQueueCapacity(queueCapacity);
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        return executor;
    }

}
