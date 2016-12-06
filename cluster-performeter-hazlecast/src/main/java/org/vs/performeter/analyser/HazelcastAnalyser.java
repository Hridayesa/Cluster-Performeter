package org.vs.performeter.analyser;

import com.hazelcast.core.IMap;
import org.springframework.stereotype.Component;
import org.vs.performeter.common.DefaultStatistics;

import javax.annotation.Resource;

/**
 * Created by karpovdc on 07.09.2015.
 */
@Component
public class HazelcastAnalyser extends AbstractAnalyser<DefaultStatistics> {

    @Resource
    private IMap testMap;

    @Override
    public void prepare() {
        testMap.clear();
    }
}
