package org.vs.performeter.hzcache.tester;

import com.hazelcast.core.IMap;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.vs.performeter.core.common.Tester;

import javax.annotation.Resource;
import java.util.Random;

/**
 * Created by karpovdc on 14.09.2015.
 */
@Component
public class HazelcastCacheTest implements Tester {
    @Resource private IMap testMap;
    private Random rn = new Random();
    @Value("${maxNamberOfCacheElements}")
    private Integer maxNumber;

    public Integer getMaxNumber() {
        return maxNumber;
    }
    public void setMaxNumber(Integer maxNumber) {
        this.maxNumber = maxNumber;
    }

    @Override
    public void doSingleTest() {
        int intKey = rn.nextInt(maxNumber);
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
