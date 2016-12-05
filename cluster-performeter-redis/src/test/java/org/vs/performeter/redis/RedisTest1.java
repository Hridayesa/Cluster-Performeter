package org.vs.performeter.redis;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import javax.annotation.Resource;

/**
 * Created by dekar on 02.12.2016.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {RedisConfig.class}, loader = AnnotationConfigContextLoader.class)
@TestPropertySource( locations = {"classpath:app.properties"}, properties = {"isTesterInstance = true"})
public class RedisTest1 {
    @Resource(name = "redisTemplate")
    RedisTemplate template;

    @Test
    public void testRedis1() throws Exception {
        HashOperations<String, String, String> hashOperations = template.opsForHash();
        hashOperations.put("WWW","fld1","value 1");
        Assert.assertEquals("value 1", hashOperations.get("WWW", "fld1"));
    }
}
