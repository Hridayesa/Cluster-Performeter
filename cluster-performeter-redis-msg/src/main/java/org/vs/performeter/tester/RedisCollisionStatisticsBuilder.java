package org.vs.performeter.tester;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.vs.performeter.common.RedisCollisionStatistics;

import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by Denis Karpov on 09.12.2016.
 */
@Component
public class RedisCollisionStatisticsBuilder implements CollisionStatisticsBuilder<RedisCollisionStatistics> {

    private AtomicLong collisionCount = new AtomicLong(0);

    @Autowired
    private DefaultStatisticsBuilder builder;

    @Override
    public long collisionPlusPlus() {
        return collisionCount.incrementAndGet();
    }

    @Override
    public void start() {
        builder.start();
    }

    @Override
    public long countPlusPlus() {
        return builder.countPlusPlus();
    }

    @Override
    public void stop() {
        builder.stop();
    }

    @Override
    public RedisCollisionStatistics getStatistics() {
        return new RedisCollisionStatistics(builder.getCount().get(),
                builder.getEndMillis()-builder.getStartMillis(),
                collisionCount.get());
    }
}
