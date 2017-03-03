package org.vs.performeter.tester;

import org.springframework.beans.factory.annotation.Autowired;
import org.vs.performeter.common.Statistics;

/**
 * Created by Denis Karpov on 05.12.2016.
 */
public abstract class AbstractTester<S extends Statistics, V extends StatisticsBuilder<S>> implements Tester<S> {

    protected ContainerManager containerManager;

    @Autowired
    protected V statisticsBuilder;

    @Override
    public void beforeTests() {
        statisticsBuilder.start();
    }

    @Override
    public void afterTests() {
        statisticsBuilder.stop();
    }

    @Override
    public S getStatistics() {
        return statisticsBuilder.getStatistics();
    }

    @Override
    public void setContainerManager(ContainerManager containerManager) {
        this.containerManager = containerManager;
    }
}
