package org.vs.performeter.tester;

import com.hazelcast.core.IMap;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.vs.performeter.common.DefaultStatistics;

import javax.annotation.Resource;
import java.util.Random;

/**
 * Created by Denis Karpov on 14.09.2015.
 */
@Component
@ConfigurationProperties(prefix = "performeter.hazlecast")
public class HazelcastCacheTest extends AbstractTester<DefaultStatistics, DefaultStatisticsBuilder> {
    @Resource private IMap testMap;
    private Random rn = new Random();
    private Integer maxNumberOfCacheElements;

    public Integer getMaxNumberOfCacheElements() {
        return maxNumberOfCacheElements;
    }
    public void setMaxNumberOfCacheElements(Integer maxNumberOfCacheElements) {
        this.maxNumberOfCacheElements = maxNumberOfCacheElements;
    }

    @Override
    public void doSingleTest() {
        statisticsBuilder.countPlusPlus();

        int intKey = rn.nextInt(maxNumberOfCacheElements);
        String key = Integer.toString(intKey);
        testMap.lock(key);
        try {

            Integer limit = (Integer) testMap.get(key);
            limit = (limit == null) ? 1 : limit + 1;
            testMap.set(key, limit);

        } finally {
            testMap.unlock(key);
        }
    }

}
