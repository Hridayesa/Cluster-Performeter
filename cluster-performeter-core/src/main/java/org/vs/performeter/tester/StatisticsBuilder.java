package org.vs.performeter.tester;

import org.vs.performeter.common.Statistics;

/**
 * Created by dekar on 06.12.2016.
 */
public interface StatisticsBuilder<V extends Statistics> {
    void start();
    void countPlusPlus();
    void stop();

    V getStatistics();
}
