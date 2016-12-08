package org.vs.performeter.tester;

import org.vs.performeter.common.Statistics;

/**
 * Created by Denis Karpov on 06.12.2016.
 */
public interface StatisticsBuilder<S extends Statistics> {

    void start();

    void countPlusPlus();

    void stop();

    S getStatistics();
}
