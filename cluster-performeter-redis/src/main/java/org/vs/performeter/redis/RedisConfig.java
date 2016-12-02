package org.vs.performeter.redis;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;

/**
 * Created by dekar on 02.12.2016.
 */
@SpringBootApplication
public class RedisConfig {

//    public RedisConnectionFactory lettuceConnectionFactory(){
//        return new LettuceConnectionFactory();
//    }
}
