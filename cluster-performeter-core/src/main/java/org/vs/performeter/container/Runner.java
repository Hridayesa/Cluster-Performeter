package org.vs.performeter.container;

import com.hazelcast.core.ICountDownLatch;
import com.hazelcast.core.IMap;
import com.hazelcast.core.ITopic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vs.performeter.common.ContextEnum;
import org.vs.performeter.common.Analyser;
import org.vs.performeter.common.MessageType;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * Created by karpovdc on 07.09.2015.
 */
public class Runner{
    private static Logger LOG = LoggerFactory.getLogger(Runner.class);

    @Resource private IMap context;
    @Resource private Analyser analyser;
    @Resource private ITopic<MessageType> topic;
    @Resource private ICountDownLatch finishCollectionLatch;

    private Integer seconds;

    public Integer getSeconds() {
        return seconds;
    }

    public void setSeconds(Integer seconds) {
        this.seconds = seconds;
    }

    @PostConstruct
    public void start() throws InterruptedException {
        LOG.info("READY !!!");
        if ( init() ) {

            topic.publish(MessageType.START);
            LOG.info("STARTED !!!");
            Thread.sleep(1000 * seconds);

            topic.publish(MessageType.STOP);
            LOG.info("STOPED !!!");

            doAnalysis();
            LOG.info("DONE !!!");
            topic.publish(MessageType.EXIT);
        }
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
}
