package org.vs.performeter.tester;

import org.apache.ignite.IgniteCache;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.vs.performeter.common.DefaultStatistics;
import org.vs.performeter.common.DefaultStatisticsBuilder;
import org.vs.performeter.data.DataProvider;
import org.vs.performeter.data.collision.CollisionStatistics;
import org.vs.performeter.data.collision.CollisionStatisticsBuilderImpl;
import org.vs.performeter.data.dummy.Msg;

import javax.annotation.Resource;
import java.util.Random;
import java.util.concurrent.locks.Lock;

/**
 * Created by Denis Karpov on 02.12.2016.
 */
@ConfigurationProperties(prefix = "performeter.ignite")
@Component
public class IgniteCacheTest extends AbstractTester<CollisionStatistics, CollisionStatisticsBuilderImpl>  {

    @Resource
    IgniteCache<String,Msg> counter;

    @Resource(name = "dummyMsgGenDataProvider")
    private DataProvider<Msg> dataProvider;

    @Override
    public void doSingleTest() {
        statisticsBuilder.countPlusPlus();
        Msg msg = dataProvider.nextData();

        statisticsBuilder.countPlusPlus();

        try {
            if (!counter.putIfAbsent(msg.getId(), msg)) {
                statisticsBuilder.collisionPlusPlus();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }
}
