package org.vs.performeter.common;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;

/**
 * Created by Denis Karpov on 02.12.2016.
 */
@Configuration
public class RedisConfiguration {

    @Bean
    @ConfigurationProperties(prefix = "performeter.redis")
    public RedisConnectionFactory lettuceConnectionFactory(){
        return new LettuceConnectionFactory();
    }
}
