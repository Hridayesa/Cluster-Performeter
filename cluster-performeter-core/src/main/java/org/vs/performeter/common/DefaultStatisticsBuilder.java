package org.vs.performeter.common;

import org.springframework.stereotype.Component;
import org.vs.performeter.tester.StatisticsBuilder;

import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by Denis Karpov on 08.09.2015.
 */
@Component
public class DefaultStatisticsBuilder implements StatisticsBuilder<DefaultStatistics> {
    private static final long serialVersionUID = -4435431292679522692L;

    private AtomicLong count = new AtomicLong(0);
    private long startMillis;
    private long endMillis;

    public AtomicLong getCount() {
        return count;
    }

    public long getStartMillis() {
        return startMillis;
    }

    public long getEndMillis() {
        return endMillis;
    }

    @Override
    public void start() {
        startMillis = System.currentTimeMillis();
    }

    public long countPlusPlus() {
        return count.incrementAndGet();
    }

    @Override
    public void stop() {
        endMillis = System.currentTimeMillis();
    }

    @Override
    public DefaultStatistics getStatistics() {
        return new DefaultStatistics(count.get(), endMillis - startMillis);
    }

}
