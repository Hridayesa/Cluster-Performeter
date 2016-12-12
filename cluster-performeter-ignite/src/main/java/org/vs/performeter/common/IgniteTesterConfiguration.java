package org.vs.performeter.common;

import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.Ignition;
import org.apache.ignite.cache.CacheAtomicityMode;
import org.apache.ignite.configuration.CacheConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by Denis Karpov on 02.12.2016.
 */
@Configuration
public class IgniteTesterConfiguration {

    @Bean
    @ConfigurationProperties(prefix = "performeter.ignite")
    public Ignite ignite(){
//        IgniteConfiguration igniteConfiguration = new IgniteConfiguration();
//        CacheConfiguration cacheConfiguration = new CacheConfiguration();
//        cacheConfiguration.setAtomicityMode(CacheAtomicityMode.TRANSACTIONAL);
//        igniteConfiguration.setCacheConfiguration(cacheConfiguration);
//        return Ignition.getOrStart(igniteConfiguration);
        return Ignition.start();
    }

    @Bean(name = "counter")
    public IgniteCache<Integer,Integer> counter() {
        CacheConfiguration cacheConfiguration = new CacheConfiguration("qqq");

        // ---  Locks works only with CacheAtomicityMode.TRANSACTIONAL
        cacheConfiguration.setAtomicityMode(CacheAtomicityMode.TRANSACTIONAL);

//        IgniteCache<Integer, Integer> qqq = ignite().createCache(cacheConfiguration);
        IgniteCache<Integer, Integer> qqq = ignite().getOrCreateCache(cacheConfiguration);
        return qqq;
    }
}
