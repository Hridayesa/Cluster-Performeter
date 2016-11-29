package org.vs.performeter.hzcache;

import com.hazelcast.core.IMap;
import org.vs.performeter.common.Tester;

import javax.annotation.Resource;
import java.util.Random;

/**
 * Created by karpovdc on 14.09.2015.
 */
public class HazelcastCacheTest implements Tester {
    @Resource private IMap testMap;
    private Random rn = new Random();
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
