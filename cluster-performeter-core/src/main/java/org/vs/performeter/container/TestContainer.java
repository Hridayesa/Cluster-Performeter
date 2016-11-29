package org.vs.performeter.container;

import com.hazelcast.core.ICountDownLatch;
import com.hazelcast.core.IMap;
import com.hazelcast.core.ITopic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.SchedulingTaskExecutor;
import org.vs.performeter.common.ContextEnum;
import org.vs.performeter.common.Statistics;
import org.vs.performeter.common.Tester;
import org.vs.performeter.common.MessageType;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

/**
 * Created by karpovdc on 07.09.2015.
 */

public class TestContainer {
    private static Logger LOG = LoggerFactory.getLogger(TestContainer.class);
    private int id;

    @Resource private IMap context;
    @Resource private ITopic<MessageType> topic;
    @Resource private IMap statisticsMap;
    @Resource private ICountDownLatch finishCollectionLatch;
    @Resource private SchedulingTaskExecutor taskExecutor;
    @Resource private Tester tester;

    private volatile boolean stopped = false;

    private Statistics statistics = new Statistics(0L,0L);

    @PostConstruct
    public void register(){
        context.lock(ContextEnum.TESTERS_COUNT);
        try {
            Integer count = (Integer) context.get(ContextEnum.TESTERS_COUNT);
            count = (count == null) ? 1 : count + 1;
            id = count;
            context.put(ContextEnum.TESTERS_COUNT, count);
            LOG.info("## ID="+id);
            topic.addMessageListener(
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
        }

        finally {
            context.unlock(ContextEnum.TESTERS_COUNT);
        }
        LOG.info("READY !!!");
    }

    private void doTest(){
        Long startMillis = System.currentTimeMillis();
        LOG.info("STARTED !!!");
        while (!stopped){
            taskExecutor.execute(() -> tester.doSingleTest());
            statistics.countPlusPlus();
        }
        LOG.info("STOPED !!!");

        saveStatistics(System.currentTimeMillis() - startMillis);

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        finishCollectionLatch.countDown();
        LOG.info("DONE !!!");
    }

    private void saveStatistics(Long millis) {
        statistics.setMilliseconds( millis) ;
        statisticsMap.put(Integer.toString(id), statistics);
        LOG.info("## Statistics saved for id=" + id);
        LOG.info(statistics.toString());
    }
}
