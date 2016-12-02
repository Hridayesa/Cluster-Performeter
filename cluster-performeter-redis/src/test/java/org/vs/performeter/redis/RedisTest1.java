package org.vs.performeter.redis;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

/**
 * Created by dekar on 02.12.2016.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {RedisConfig.class}, loader = AnnotationConfigContextLoader.class)
@TestPropertySource( locations = {"classpath:app.properties"}, properties = {"isTesterInstance = true"})
public class RedisTest1 {
    @Autowired
    StringRedisTemplate template;

    @Test
    public void testRedis1() throws Exception {
        template.opsForHash().put("WWW","fld1","value 1");
        Assert.assertEquals("value 1", template.opsForHash().get("WWW", "fld1"));
    }
}
