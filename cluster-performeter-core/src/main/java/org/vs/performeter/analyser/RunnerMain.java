package org.vs.performeter.analyser;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Import;
import org.vs.performeter.common.CommonConfiguration;

/**
 * Created by Denis Karpov on 10.09.2015.
 */
@SpringBootApplication
@Import(CommonConfiguration.class)
public class RunnerMain {
    public static void main(String[] args) throws InterruptedException {
        ApplicationContext context = SpringApplication.run(RunnerMain.class, args);
        context.getBean(Orchestrator.class).run();
        while (true){
            Thread.sleep(2000);
        }
    }
}
