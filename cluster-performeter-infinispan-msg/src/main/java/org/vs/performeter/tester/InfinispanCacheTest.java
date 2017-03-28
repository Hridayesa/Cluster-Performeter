package org.vs.performeter.tester;

import org.infinispan.Cache;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.vs.performeter.common.DefaultStatistics;
import org.vs.performeter.common.DefaultStatisticsBuilder;
import org.vs.performeter.data.DataProvider;
import org.vs.performeter.data.Probe;
import org.vs.performeter.data.collision.CollisionStatistics;
import org.vs.performeter.data.collision.CollisionStatisticsBuilderImpl;
import org.vs.performeter.data.dummy.Msg;

import javax.annotation.Resource;
import java.util.Random;

/**
 * Created by Denis Karpov on 14.09.2015.
 */
@Component
@ConfigurationProperties(prefix = "performeter.infinispan")
public class InfinispanCacheTest extends AbstractTester<CollisionStatistics, CollisionStatisticsBuilderImpl> {

    @Resource(name = "testMap")
    private Cache<String, Msg> testMap;
    @Resource(name = "dummyMsgGenDataProvider")
    private DataProvider<Msg> dataProvider;
    int i=0;

    @Override
    public void beforeTests() {
        super.beforeTests();
        dataProvider.open(containerManager.getId());
    }

    @Override
    public void afterTests() {
        dataProvider.close();
        super.afterTests();
    }


    @Override
    public void doSingleTest() {
        Msg obj = dataProvider.nextData();
        if (obj == null) {
            containerManager.stop();
            return;
        }
        statisticsBuilder.countPlusPlus();

        if (testMap.putIfAbsent(obj.getId(), obj) != null) {
            statisticsBuilder.collisionPlusPlus();
        }
    }

}
