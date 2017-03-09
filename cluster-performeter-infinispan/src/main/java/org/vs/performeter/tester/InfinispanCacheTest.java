package org.vs.performeter.tester;

import org.infinispan.Cache;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.vs.performeter.common.DefaultStatistics;
import org.vs.performeter.common.DefaultStatisticsBuilder;

import javax.annotation.Resource;
import java.util.Random;

/**
 * Created by Denis Karpov on 14.09.2015.
 */
@Component
@ConfigurationProperties(prefix = "performeter.infinispan")
public class InfinispanCacheTest extends AbstractTester<DefaultStatistics, DefaultStatisticsBuilder> {

    @Resource(name = "testMap")
    private Cache<Integer, Integer> testMap;
    private Random random = new Random();
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
        int key = random.nextInt(maxNumberOfCacheElements);

        try {
            while (true) {
                Integer oldCount = testMap.get(key);
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
