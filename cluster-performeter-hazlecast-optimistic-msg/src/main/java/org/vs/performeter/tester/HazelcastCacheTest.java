package org.vs.performeter.tester;

import com.hazelcast.core.IMap;
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

/**
 * Created by Denis Karpov on 14.09.2015.
 */
@Component
@ConfigurationProperties(prefix = "performeter.hazlecast")
public class HazelcastCacheTest extends AbstractTester<CollisionStatistics, CollisionStatisticsBuilderImpl> {
    @Resource
    private IMap testMap;
    private Random random = new Random();

    @Resource(name = "dummyMsgGenDataProvider")
    private DataProvider<Msg> dataProvider;


    @Override
    public void doSingleTest() {
        try {
            statisticsBuilder.countPlusPlus();
            Msg msg = dataProvider.nextData();

            if (testMap.putIfAbsent(msg.getId(), msg) != null) {
                statisticsBuilder.collisionPlusPlus();
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }

    }
}