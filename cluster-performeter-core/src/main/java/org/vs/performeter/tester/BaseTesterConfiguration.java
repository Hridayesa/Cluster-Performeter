package org.vs.performeter.tester;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.vs.performeter.common.CommonConfiguration;

/**
 * Created by dekar on 06.12.2016.
 */
@Configuration
@ComponentScan
@Import(CommonConfiguration.class)
public class BaseTesterConfiguration {
}
