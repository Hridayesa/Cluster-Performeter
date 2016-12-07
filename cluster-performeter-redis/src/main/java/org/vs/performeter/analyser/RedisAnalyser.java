package org.vs.performeter.analyser;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.vs.performeter.common.DefaultStatistics;

import javax.annotation.Resource;

/**
 * Created by Denis Karpov on 06.12.2016.
 */
@Component
public class RedisAnalyser extends AbstractAnalyser<DefaultStatistics> {

    @Resource(name = "redisTemplate")
    RedisTemplate template;

    @Override
    public void prepare() {
        template.delete("QQQ");
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
