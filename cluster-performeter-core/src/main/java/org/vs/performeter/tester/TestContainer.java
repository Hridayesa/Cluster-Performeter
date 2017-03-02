package org.vs.performeter.tester;

import com.hazelcast.core.ICountDownLatch;
import com.hazelcast.core.IMap;
import com.hazelcast.core.ITopic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.vs.performeter.common.ContextEnum;
import org.vs.performeter.common.MessageType;
import org.vs.performeter.common.Statistics;

import javax.annotation.Resource;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by Denis Karpov on 07.09.2015.
 */
@Component
//@ConditionalOnProperty("isTesterInstance")
public class TestContainer implements Runnable {
    private static Logger LOG = LoggerFactory.getLogger(TestContainer.class);
    private int id;

    @Resource
    private IMap context;
    @Resource
    private ITopic<MessageType> controlTopic;
    @Resource
    private IMap statisticsMap;
    @Resource
    private ICountDownLatch finishCollectionLatch;
    @Resource
    private ExecutorService taskExecutor;

    @Resource
    private Tester tester;

    private volatile boolean stopped = false;

//    private Statistics statistics = new DefaultStatistics(0L,0L);

    public void run() {
        context.lock(ContextEnum.TESTERS_COUNT);
        try {
            Integer count = (Integer) context.get(ContextEnum.TESTERS_COUNT);
            count = (count == null) ? 1 : count + 1;
            id = count;
            context.put(ContextEnum.TESTERS_COUNT, count);
            LOG.info("## ID=" + id);
            controlTopic.addMessageListener(
                    message -> {
                        switch (message.getMessageObject()) {
                            case START:
                                stopped = false;
                                new Thread(() -> doTest()).start();
                                break;
                            case STOP:
                                stopped = true;
                                break;
                            case EXIT:
                                System.exit(0);
                                break;
                        }
                    });
        } finally {
            context.unlock(ContextEnum.TESTERS_COUNT);
        }
        LOG.info("READY !!!");
    }

    private void doTest() {
//        Long startMillis = System.currentTimeMillis();
        tester.beforeTests();
        LOG.info("STARTED !!!");
        while (!stopped) {
            taskExecutor.submit(() -> tester.doSingleTest());
//            statistics.countPlusPlus();
        }
        taskExecutor.shutdown();
        try {
            taskExecutor.awaitTermination(5, TimeUnit.SECONDS);
            tester.afterTests();
            LOG.info("STOPED !!!");

            saveStatistics(tester.getStatistics());

    //        saveStatistics(System.currentTimeMillis() - startMillis);

            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        finishCollectionLatch.countDown();
        LOG.info("DONE !!!");
    }


    private void saveStatistics(Statistics statistics) {
        statisticsMap.put(Integer.toString(id), statistics);
        LOG.info("## Statistics saved for id=" + id);
        LOG.info(statistics.toString());
    }
}
