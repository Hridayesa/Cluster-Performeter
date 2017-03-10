package org.vs.performeter.common;

import org.infinispan.Cache;
import org.infinispan.configuration.cache.CacheMode;
import org.infinispan.configuration.cache.ConfigurationBuilder;
import org.infinispan.configuration.global.GlobalConfiguration;
import org.infinispan.configuration.global.GlobalConfigurationBuilder;
import org.infinispan.manager.DefaultCacheManager;
import org.infinispan.manager.EmbeddedCacheManager;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.vs.performeter.data.Probe;

/**
 * Created by Denis Karpov on 06.12.2016.
 */
@Configuration
@ConfigurationProperties(prefix = "performeter.infinispan")
@ComponentScan({"org.vs.performeter.data.providers",
        "org.vs.performeter.data.collision"})
public class InfinispanConfiguration {

    @Bean
    @Scope(scopeName = "singleton")
    public EmbeddedCacheManager manager() {
        // TODO Do correct configuration
//        org.infinispan.configuration.cache.Configuration
//                configuration = new ConfigurationBuilder().transaction().lockingMode(LockingMode.PESSIMISTIC).build();
//        return new DefaultCacheManager(configuration);
        GlobalConfiguration globalConfig = new GlobalConfigurationBuilder()
                .transport().defaultTransport()
                .addProperty("configurationFile", "default-configs/default-jgroups-tcp.xml")
                .build();
        org.infinispan.configuration.cache.Configuration defaultConfiguration = new ConfigurationBuilder()
                .clustering()
                .cacheMode(CacheMode.DIST_SYNC)
                .hash()
                .numOwners(2)
                .numSegments(100)
                .capacityFactor(2)
                .build();
//        org.infinispan.configuration.cache.Configuration c = new ConfigurationBuilder().clustering().cacheMode(CacheMode.REPL_SYNC).build();
        return new DefaultCacheManager(globalConfig, defaultConfiguration);
    }

    @Bean("testMap")
    @Scope(scopeName = "singleton")
    public Cache<String, Probe> testMap() {
        return manager().getCache("testMap", true);
    }
}
