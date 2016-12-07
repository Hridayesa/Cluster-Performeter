package org.vs.performeter.tester;

import org.vs.performeter.common.Statistics;

/**
 * Created by Denis Karpov on 14.09.2015.
 */
public interface Tester<S extends Statistics>{
    void doSingleTest();
    void beforeTests();
    void afterTests();

    S getStatistics();
}
