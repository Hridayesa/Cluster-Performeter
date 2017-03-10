package org.vs.performeter.analyser;

import org.infinispan.Cache;
import org.springframework.stereotype.Component;
import org.vs.performeter.common.DefaultStatistics;
import org.vs.performeter.data.Probe;

import javax.annotation.Resource;

/**
 * Created by Denis Karpov on 07.09.2015.
 */
@Component
public class InfinispanAnalyser extends AbstractAnalyser<DefaultStatistics> {

    @Resource(name = "testMap")
    private Cache<String, Probe> testMap;;

    @Override
    public void prepare() {
        testMap.clear();
    }
}
