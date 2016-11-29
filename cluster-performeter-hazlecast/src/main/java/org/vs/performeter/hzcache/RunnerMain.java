package org.vs.performeter.hzcache;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by karpovdc on 10.09.2015.
 */
public class RunnerMain {
    public static void main(String[] args) throws InterruptedException {
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("/core/parent.cfg.xml","/core/analyser.cfg.xml","/hazelcast-test/hazelcast.cfg.xml");
        while (true){
            Thread.sleep(2000);
        }
    }
}
