package org.vs.performeter.common;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisSentinelConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;

/**
 * Created by Denis Karpov on 06.12.2016.
 */
@Configuration
@ComponentScan({"org.vs.performeter.data.providers",
                "org.vs.performeter.data.collision"})
public class RedisConfiguration {
    @Bean
    public RedisConnectionFactory jedisConnectionFactory() {
        RedisSentinelConfiguration sentinelConfig = new RedisSentinelConfiguration() .master("mymaster")
                .sentinel("192.168.56.63", 26379);
        return new JedisConnectionFactory(sentinelConfig);
    }
}
