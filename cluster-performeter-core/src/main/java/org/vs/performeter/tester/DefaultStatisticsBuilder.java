package org.vs.performeter.tester;

import org.springframework.stereotype.Component;
import org.vs.performeter.common.DefaultStatistics;

import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by karpovdc on 08.09.2015.
 */
@Component
public class DefaultStatisticsBuilder implements StatisticsBuilder<DefaultStatistics>{
    private static final long serialVersionUID = -4435431292679522692L;

    protected AtomicLong count = new AtomicLong(0);
    protected long startMillis;
    protected long endMillis;

    @Override
    public void start() {
        startMillis = System.currentTimeMillis();
    }

    public void countPlusPlus(){
        count.getAndIncrement();
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
