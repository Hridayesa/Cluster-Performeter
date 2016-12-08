package org.vs.performeter.analyser;

import org.vs.performeter.common.Statistics;

import java.util.Map;

/**
 * Created by Denis Karpov on 14.09.2015.
 */
public interface Analyser<S extends Statistics> {

    void prepare();

    void analyse(Map<String, S> statistics);
}
