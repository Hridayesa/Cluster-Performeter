package org.vs.performeter.data.dummy;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.vs.performeter.data.DataProvider;

import java.util.Random;

/**
 * Created by Denis Karpov on 21.02.2017.
 */
@Component
@ConfigurationProperties(prefix = "performeter.dataprovider")
public class DummyMsgGenDataProvider implements DataProvider<Msg> {
    private Random random = new Random();

    private String blob;
    private Integer maxNumberOfCacheElements;

    public String getBlob() {
        return blob;
    }
    public void setBlob(String blob) {
        this.blob = blob;
    }

    public Integer getMaxNumberOfCacheElements() {
        return maxNumberOfCacheElements;
    }
    public void setMaxNumberOfCacheElements(Integer maxNumberOfCacheElements) {
        this.maxNumberOfCacheElements = maxNumberOfCacheElements;
    }

    @Override
    public Msg nextData() {
        String id = Integer.toString(random.nextInt(maxNumberOfCacheElements));
        return new Msg(id, "name" + id, getBlob());
    }
}
