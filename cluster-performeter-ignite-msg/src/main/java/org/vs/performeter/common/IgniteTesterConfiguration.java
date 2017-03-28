package org.vs.performeter.common;

import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.Ignition;
import org.apache.ignite.cache.CacheAtomicityMode;
import org.apache.ignite.configuration.CacheConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.vs.performeter.data.dummy.Msg;

/**
 * Created by Denis Karpov on 02.12.2016.
 */
@Configuration
@ComponentScan({"org.vs.performeter.data.dummy",
        "org.vs.performeter.data.collision"})
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
    public IgniteCache<String,Msg>  counter() {
        CacheConfiguration cacheConfiguration = new CacheConfiguration("qqq");

        // ---  Locks works only with CacheAtomicityMode.TRANSACTIONAL
        cacheConfiguration.setAtomicityMode(CacheAtomicityMode.TRANSACTIONAL);

//        IgniteCache<Integer, Integer> qqq = ignite().createCache(cacheConfiguration);
        IgniteCache<String,Msg>  qqq = ignite().getOrCreateCache(cacheConfiguration);
        return qqq;
    }
}
