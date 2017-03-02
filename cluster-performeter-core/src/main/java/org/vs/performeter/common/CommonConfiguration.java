package org.vs.performeter.common;

import com.hazelcast.core.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.*;

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

//    @ConfigurationProperties(prefix = "performeter.executor")
//    @Bean(initMethod = "initialize")
//    public Executor taskExecutor() {
//        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
//        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
//        return executor;
//    }

    //@ConfigurationProperties(prefix = "performeter.executor")
    @Bean
    public ExecutorService taskExecutor(@Value("${performeter.executor.corePoolSize}") int corePoolSize) {
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(corePoolSize, corePoolSize,
                0L, TimeUnit.MILLISECONDS,
                new SynchronousQueue<Runnable>(),
                new BlockCallerPolicy());
        return threadPoolExecutor;
    }

    protected static class BlockCallerPolicy implements RejectedExecutionHandler {
        public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
            if (executor.isShutdown()) {
                throw new RejectedExecutionException("executor has been shutdown");
            } else {
                try {
                    executor.getQueue().put(r);
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                    RejectedExecutionException e = new RejectedExecutionException("interrupted");
                    e.initCause(ie);
                    throw e;
                }
            }
        }
    }
}
