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
    @Resource
    private IMap testMap;
    private Random random = new Random();
    private Integer maxNamberOfCacheElements;

    public Integer getMaxNamberOfCacheElements() {
        return maxNamberOfCacheElements;
    }

    public void setMaxNamberOfCacheElements(Integer maxNamberOfCacheElements) {
        this.maxNamberOfCacheElements = maxNamberOfCacheElements;
    }

    @Override
    public void doSingleTest() {
        statisticsBuilder.countPlusPlus();

        int intKey = random.nextInt(maxNamberOfCacheElements);
        String key = Integer.toString(intKey);

        try {
            while (true) {
                Integer oldCount = (Integer) testMap.get(key);
                if (oldCount == null) {
                    if (testMap.putIfAbsent(key, 1) == null) {
                        break;
                    }
                } else {
                    if (testMap.replace(key, oldCount, oldCount + 1)) {
                        break;
                    }
                }
                Thread.sleep(10);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

}
