package org.vs.performeter.tester;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.PropertySource;

/**
 * Created by dekar on 02.12.2016.
 */
@SpringBootApplication
//@ComponentScan({"org.vs.performeter.core", "org.vs.performeter.tester"})
@PropertySource( value = {"classpath:app.properties", "file:../config/app.properties"},ignoreResourceNotFound = true)
public class TesterMain {
    public static void main(String[] args) throws InterruptedException {
//        System.setProperty("isTesterInstance", "true");
        ApplicationContext context = SpringApplication.run(TesterMain.class, args);
        context.getBean(TestContainer.class).run();
//        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("/core/parent.cfg.xml","/core/tester.cfg.xml","/hazelcast-test/hazelcast.cfg.xml");
        while (true){
            Thread.sleep(2000);
        }
    }
}
