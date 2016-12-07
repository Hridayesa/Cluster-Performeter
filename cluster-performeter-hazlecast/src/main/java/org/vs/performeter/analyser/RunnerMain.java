package org.vs.performeter.analyser;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

/**
 * Created by Denis Karpov on 10.09.2015.
 */
@SpringBootApplication
public class RunnerMain {
    public static void main(String[] args) throws InterruptedException {
        ApplicationContext context = SpringApplication.run(RunnerMain.class, args);
        context.getBean(Orchestrator.class).run();
        while (true){
            Thread.sleep(2000);
        }
    }
}
