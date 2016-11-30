package org.vs.performeter.hzcache.tester;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.vs.performeter.core.CommonConfiguration;
import org.vs.performeter.core.container.TestContainer;

/**
 * Created by karpovdc on 01.09.2015.
 */
@SpringBootApplication
@ComponentScan({"org.vs.performeter.core", "org.vs.performeter.hzcache.tester"})
@PropertySource( value = {"classpath:app.properties", "file:../config/app.properties"},ignoreResourceNotFound = true)
public class TesterMain {
    public static void main(String[] args) throws InterruptedException {
        System.setProperty("isTesterInstance", "true");
        ApplicationContext context = SpringApplication.run(TesterMain.class, args);
        context.getBean(TestContainer.class).run();
//        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("/core/parent.cfg.xml","/core/tester.cfg.xml","/hazelcast-test/hazelcast.cfg.xml");
        while (true){
            Thread.sleep(2000);
        }
    }

    @Bean("testMap")
    public IMap testMap(@Autowired HazelcastInstance hazelcastInstance){
        return hazelcastInstance.getMap("testMap");
    }

}
