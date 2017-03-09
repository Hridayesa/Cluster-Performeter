package org.vs.performeter.data.collision;

import org.vs.performeter.common.Statistics;
import org.vs.performeter.tester.StatisticsBuilder;

/**
 * Created by Denis Karpov on 09.12.2016.
 */
public interface CollisionStatisticsBuilder<S extends Statistics> extends StatisticsBuilder<S> {
    long collisionPlusPlus();
}
