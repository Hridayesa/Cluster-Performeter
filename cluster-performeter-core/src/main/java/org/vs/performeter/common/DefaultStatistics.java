package org.vs.performeter.common;

/**
 * Created by Denis Karpov on 06.12.2016.
 */
public class DefaultStatistics implements Statistics {
    private static final long serialVersionUID = 5789873333236157052L;

    private final long count;
    private final long milliseconds;

    public DefaultStatistics(long count, long milliseconds) {
        this.count = count;
        this.milliseconds = milliseconds;
    }

    @Override
    public Long getCount() {
        return count;
    }

    @Override
    public Long getMilliseconds() {
        return milliseconds;
    }

    @Override
    public String toString() {
        return "DefaultStatistics{" +
                "count=" + count +
                ", milliseconds=" + milliseconds +
                '}';
    }
}
