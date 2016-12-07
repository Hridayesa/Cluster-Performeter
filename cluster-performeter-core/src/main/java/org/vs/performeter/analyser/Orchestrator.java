package org.vs.performeter.analyser;

import com.hazelcast.core.ICountDownLatch;
import com.hazelcast.core.IMap;
import com.hazelcast.core.ITopic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.vs.performeter.common.ContextEnum;
import org.vs.performeter.common.MessageType;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * Created by Denis Karpov on 07.09.2015.
 */
@Component
@ConfigurationProperties(prefix = "performeter.orchestrator")
//@ConditionalOnProperty("isOrchestrator")
public class Orchestrator implements Runnable{
    private static Logger LOG = LoggerFactory.getLogger(Orchestrator.class);

    @Resource private Analyser analyser;

    @Resource private IMap context;
    @Resource private ITopic<MessageType> controlTopic;
    @Resource private ICountDownLatch finishCollectionLatch;
    @Resource private IMap statisticsMap;

    private Integer testDurationSeconds;

    public Integer getTestDurationSeconds() {
        return testDurationSeconds;
    }
    public void setTestDurationSeconds(Integer testDurationSeconds) {
        this.testDurationSeconds = testDurationSeconds;
    }

    private boolean init() {
        Integer counts = (Integer)context.get(ContextEnum.TESTERS_COUNT);
        LOG.info("## test application count="+counts);
        if (counts!=null) {
            finishCollectionLatch.trySetCount(counts);
        }

        analyser.prepare();

        return counts!=null;
    }

    private void doAnalysis() throws InterruptedException {
        finishCollectionLatch.await(10, TimeUnit.SECONDS);
        LOG.info("Do analysis");
        analyser.analyse(statisticsMap);
    }

    @Override
    public void run() {
        LOG.info("READY !!!");
        if ( init() ) {
            try {
                controlTopic.publish(MessageType.START);
                LOG.info("STARTED !!!");
                Thread.sleep(1000 * testDurationSeconds);

                controlTopic.publish(MessageType.STOP);
                LOG.info("STOPED !!!");

                doAnalysis();
                LOG.info("DONE !!!");
                controlTopic.publish(MessageType.EXIT);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
