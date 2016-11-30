package org.vs.performeter.hzcache.runner;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.vs.performeter.core.CommonConfiguration;
import org.vs.performeter.core.container.Runner;

/**
 * Created by karpovdc on 10.09.2015.
 */
@SpringBootApplication
@ComponentScan({"org.vs.performeter.core", "org.vs.performeter.hzcache.runner"})
@PropertySource( value = {"classpath:app.properties", "file:../config/app.properties"},ignoreResourceNotFound = true)
public class RunnerMain {
    public static void main(String[] args) throws InterruptedException {
        System.setProperty("isAnaliserInstance", "true");
        ApplicationContext context = SpringApplication.run(RunnerMain.class, args);
        context.getBean(Runner.class).run();
//        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("/core/parent.cfg.xml","/core/analyser.cfg.xml","/hazelcast-test/hazelcast.cfg.xml");
        while (true){
            Thread.sleep(2000);
        }
    }
}
