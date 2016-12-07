package org.vs.performeter.analyser;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.vs.performeter.common.CommonConfiguration;

/**
 * Created by Denis Karpov on 06.12.2016.
 */
@Configuration
@ComponentScan
@Import(CommonConfiguration.class)
public class BaseOrchestratorConfiguration {
}
