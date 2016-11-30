package org.vs.performeter.core.common;

import java.io.Serializable;

/**
 * Created by karpovdc on 08.09.2015.
 */
public class Statistics implements Serializable{
    private static final long serialVersionUID = -4435431292679522692L;
    private long count;
    private Long milliseconds;

    public Statistics(Long count, Long milliseconds) {
        this.count = count;
        this.milliseconds = milliseconds;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }

    public Long getMilliseconds() {
        return milliseconds;
    }

    public void setMilliseconds(Long milliseconds) {
        this.milliseconds = milliseconds;
    }

    public void countPlusPlus(){
        count++;
    }

    @Override
    public String toString() {
        return "Statistics{" +
                "count=" + count +
                ", milliseconds=" + milliseconds +
                '}';
    }
}
