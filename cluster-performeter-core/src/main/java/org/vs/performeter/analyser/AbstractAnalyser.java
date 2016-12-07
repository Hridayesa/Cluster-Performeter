package org.vs.performeter.analyser;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vs.performeter.common.Statistics;

import java.util.Map;

/**
 * Created by Denis Karpov on 02.12.2016.
 */
public abstract class AbstractAnalyser<S extends Statistics> implements Analyser<S> {
    private static Logger LOG = LoggerFactory.getLogger(AbstractAnalyser.class);

    public void analyse(Map<String, S> statistics) {
        LOG.info("## statisticsMap.size()=" + statistics.size());
        statistics.entrySet().forEach(
                entry -> LOG.info("### " + entry.getKey() + "/" + entry.getValue())
        );
    }
}
