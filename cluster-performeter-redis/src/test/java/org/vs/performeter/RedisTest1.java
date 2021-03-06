package org.vs.performeter;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTestContextBootstrapper;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.BootstrapWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.vs.performeter.tester.TesterMain;

import javax.annotation.Resource;

/**
 * Created by Denis Karpov on 02.12.2016.
 */
@Ignore("Работает только при подключеном кластере Redis")
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {TesterMain.class})
@BootstrapWith(SpringBootTestContextBootstrapper.class)
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
