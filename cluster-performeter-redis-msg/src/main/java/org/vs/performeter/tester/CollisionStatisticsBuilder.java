package org.vs.performeter.tester;

import org.vs.performeter.common.Statistics;

/**
 * Created by Denis Karpov on 09.12.2016.
 */
public interface CollisionStatisticsBuilder<S extends Statistics> extends StatisticsBuilder<S> {
    long collisionPlusPlus();
}
