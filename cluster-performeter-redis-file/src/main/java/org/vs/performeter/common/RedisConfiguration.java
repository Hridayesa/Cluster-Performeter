package org.vs.performeter.common;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisSentinelConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;

/**
 * Created by Denis Karpov on 06.12.2016.
 */
@Configuration
@ComponentScan({"org.vs.performeter.data.providers",
                "org.vs.performeter.data.collision"})
@ConfigurationProperties(prefix = "performeter.redis")
public class RedisConfiguration {
    private String host;
    private int port;

    //    @Bean
//    public RedisConnectionFactory jedisConnectionFactory() {
//        RedisSentinelConfiguration sentinelConfig = new RedisSentinelConfiguration() .master("mymaster")
//                .sentinel(host, port);
//        return new JedisConnectionFactory(sentinelConfig);
//    }
    @Bean
    public LettuceConnectionFactory lettuceConnectionFactory() {
        LettuceConnectionFactory lettuceConnectionFactory = new LettuceConnectionFactory();
        lettuceConnectionFactory.setHostName(host);
        lettuceConnectionFactory.setPort(port);
        return lettuceConnectionFactory;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }
}
