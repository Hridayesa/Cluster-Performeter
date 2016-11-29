package org.vs.performeter.hzcache;

import com.hazelcast.core.IMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vs.performeter.common.Analyser;

import javax.annotation.Resource;
import java.util.Map;
import java.util.Set;

/**
 * Created by karpovdc on 07.09.2015.
 */
public class HazelcastCacheTestAnalyser implements Analyser{
    private static Logger LOG = LoggerFactory.getLogger(HazelcastCacheTestAnalyser.class);

    @Resource private IMap statisticsMap;

    public void analyse(){
        LOG.info("## statisticsMap.size()="+statisticsMap.size());
        ((Set<Map.Entry>)statisticsMap.entrySet()).forEach(
                entry -> LOG.info("### "+entry.getKey() + "/" + entry.getValue())
        );
    }
}
