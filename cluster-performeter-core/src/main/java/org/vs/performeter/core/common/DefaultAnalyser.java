package org.vs.performeter.core.common;

import com.hazelcast.core.IMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Map;
import java.util.Set;

/**
 * Created by dekar on 02.12.2016.
 */
@Component
public class DefaultAnalyser implements Analyser {
    private static Logger LOG = LoggerFactory.getLogger(DefaultAnalyser.class);

    @Resource
    private IMap statisticsMap;

    public void analyse(){
        LOG.info("## statisticsMap.size()="+statisticsMap.size());
        ((Set<Map.Entry>)statisticsMap.entrySet()).forEach(
                entry -> LOG.info("### "+entry.getKey() + "/" + entry.getValue())
        );
    }
}
