package org.vs.performeter.tester;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

/**
 * Created by Denis Karpov on 01.09.2015.
 */
@SpringBootApplication
public class TesterMain {
    public static void main(String[] args) throws InterruptedException {
        ApplicationContext context = SpringApplication.run(TesterMain.class, args);
        context.getBean(TestContainer.class).run();
        while (true){
            Thread.sleep(2000);
        }
    }
}
