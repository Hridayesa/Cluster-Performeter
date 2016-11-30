package org.vs.performeter.geode;

import com.hazelcast.core.IMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vs.performeter.core.common.Analyser;

import javax.annotation.Resource;
import java.util.Map;
import java.util.Set;

/**
 * Created by karpovdc on 03.11.2015.
 */
public class GeodeAnalyser implements Analyser {
    private static Logger LOG = LoggerFactory.getLogger(GeodeAnalyser.class);

    @Resource private IMap statisticsMap;

    @Override
    public void analyse() {
        LOG.info("## statisticsMap.size()="+statisticsMap.size());
        ((Set<Map.Entry>)statisticsMap.entrySet()).forEach(
                entry -> LOG.info("############## "+entry.getKey() + "/" + entry.getValue())
        );
    }
}
