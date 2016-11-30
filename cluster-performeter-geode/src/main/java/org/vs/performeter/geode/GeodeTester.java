package org.vs.performeter.geode;

import org.apache.geode.cache.CacheTransactionManager;
import org.apache.geode.cache.Region;
import org.apache.geode.cache.client.ClientCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vs.performeter.core.common.Tester;

import javax.annotation.Resource;
import java.util.Random;

/**
 * Created by karpovdc on 03.11.2015.
 */
public class GeodeTester implements Tester {
    private static Logger LOG = LoggerFactory.getLogger(GeodeTester.class);

    private Random rn = new Random();
    private Integer maxNumber;

    @Resource private ClientCache cache;
    @Resource private Region<String, Integer> region;

    public Integer getMaxNumber() {
        return maxNumber;
    }
    public void setMaxNumber(Integer maxNumber) {
        this.maxNumber = maxNumber;
    }

    public Region<String, Integer> getRegion() {
        return region;
    }
    public void setRegion(Region<String, Integer> region) {
        this.region = region;
    }

    @Override
    public void doSingleTest() {
        int intKey = rn.nextInt(maxNumber);
        String key = Integer.toString(intKey);
        CacheTransactionManager transactionManager = cache.getCacheTransactionManager();
        transactionManager.begin();
//        Lock lock = region.getDistributedLock(key);
//        lock.lock();
        try {
            Integer limit = region.get(key);
            limit = (limit == null) ? 1 : limit + 1;
            region.put(key, limit);
            transactionManager.commit();
        }
        catch (Exception e){
            LOG.error("!!! TRANSACTION ROLLBACK", e);
//            transactionManager.rollback();
//        } finally {
//            lock.unlock();
        }
    }
}
