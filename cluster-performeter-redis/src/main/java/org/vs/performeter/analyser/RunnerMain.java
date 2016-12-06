package org.vs.performeter.analyser;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.PropertySource;

/**
 * Created by dekar on 02.12.2016.
 */
@SpringBootApplication
//@ComponentScan({"org.vs.performeter.core", "org.vs.performeter.redis"})
@PropertySource( value = {"classpath:app.properties", "file:../config/app.properties"},ignoreResourceNotFound = true)
public class RunnerMain {
    public static void main(String[] args) throws InterruptedException {
//        System.setProperty("isOrchestrator", "true");
        ApplicationContext context = SpringApplication.run(RunnerMain.class, args);
        context.getBean(Orchestrator.class).run();
//        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("/core/parent.cfg.xml","/core/analyser.cfg.xml","/hazelcast-test/hazelcast.cfg.xml");
        while (true){
            Thread.sleep(2000);
        }
    }
}
