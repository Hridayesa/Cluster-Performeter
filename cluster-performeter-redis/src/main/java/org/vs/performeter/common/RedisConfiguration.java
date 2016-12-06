package org.vs.performeter.common;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
//import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;

/**
 * Created by dekar on 02.12.2016.
 */
@Configuration
public class RedisConfiguration {

    @Bean
    public RedisConnectionFactory lettuceConnectionFactory(){
        System.out.println("#############################");
        LettuceConnectionFactory lettuceConnectionFactory = new LettuceConnectionFactory();
        lettuceConnectionFactory.setHostName("localhost");
        lettuceConnectionFactory.setPort(6379);
        return lettuceConnectionFactory;
    }
}
