package org.vs.performeter.tester;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

/**
 * Created by Denis Karpov on 02.12.2016.
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
