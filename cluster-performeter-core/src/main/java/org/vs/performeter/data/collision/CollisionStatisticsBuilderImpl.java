package org.vs.performeter.data.collision;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.vs.performeter.common.DefaultStatisticsBuilder;

import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by Denis Karpov on 09.12.2016.
 */
@Component
public class CollisionStatisticsBuilderImpl implements CollisionStatisticsBuilder<CollisionStatistics> {

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
    public CollisionStatistics getStatistics() {
        return new CollisionStatistics(builder.getCount().get(),
                builder.getEndMillis()-builder.getStartMillis(),
                collisionCount.get());
    }
}
