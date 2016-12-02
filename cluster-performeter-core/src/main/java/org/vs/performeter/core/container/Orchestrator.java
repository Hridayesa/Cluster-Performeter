package org.vs.performeter.core.container;

import com.hazelcast.core.ICountDownLatch;
import com.hazelcast.core.IMap;
import com.hazelcast.core.ITopic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import org.vs.performeter.core.common.Analyser;
import org.vs.performeter.core.common.ContextEnum;
import org.vs.performeter.core.common.MessageType;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * Created by karpovdc on 07.09.2015.
 */
@Component
@ConditionalOnProperty("isOrchestrator")
//@ConditionalOnBean(type = {"org.vs.performeter.hzcache.orchestrator.RunnerMain"})
public class Orchestrator implements Runnable{
    private static Logger LOG = LoggerFactory.getLogger(Orchestrator.class);

    @Resource private IMap context;
    @Resource private Analyser analyser;
    @Resource private ITopic<MessageType> topic;
    @Resource private ICountDownLatch finishCollectionLatch;

    @Value("${testDurationSeconds}")
    private Integer seconds;

    public Integer getSeconds() {
        return seconds;
    }
    public void setSeconds(Integer seconds) {
        this.seconds = seconds;
    }

    private boolean init() {
        Integer counts = (Integer)context.get(ContextEnum.TESTERS_COUNT);
        LOG.info("## test application count="+counts);
        if (counts!=null) {
            finishCollectionLatch.trySetCount(counts);
        }
        return counts!=null;
    }

    private void doAnalysis() throws InterruptedException {
        finishCollectionLatch.await(10, TimeUnit.SECONDS);
        LOG.info("Do analysis");
        analyser.analyse();
    }

    @Override
    public void run() {
        LOG.info("READY !!!");
        if ( init() ) {
            try {
                topic.publish(MessageType.START);
                LOG.info("STARTED !!!");
                Thread.sleep(1000 * seconds);

                topic.publish(MessageType.STOP);
                LOG.info("STOPED !!!");

                doAnalysis();
                LOG.info("DONE !!!");
                topic.publish(MessageType.EXIT);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
