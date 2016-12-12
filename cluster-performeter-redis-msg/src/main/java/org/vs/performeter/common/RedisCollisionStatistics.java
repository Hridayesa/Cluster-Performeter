package org.vs.performeter.common;

/**
 * Created by Denis Karpov on 09.12.2016.
 */
public class RedisCollisionStatistics extends DefaultStatistics {
    private static final long serialVersionUID = -8749326216340280890L;

    private long collisionCount;

    public long getCollisionCount() {
        return collisionCount;
    }

    public void setCollisionCount(long collisionCount) {
        this.collisionCount = collisionCount;
    }

    public RedisCollisionStatistics(long count, long milliseconds, long collisionCount) {
        super(count, milliseconds);
        this.collisionCount = collisionCount;
    }

    public RedisCollisionStatistics(long count, long milliseconds) {
        super(count, milliseconds);
    }

    @Override
    public String toString() {
        return "RedisCollisionStatistics{" +
                " COUNT=" + getCount() +
                ", collisionCount=" + collisionCount +
                ", milliseconds=" + getMilliseconds()+
                "} ";
    }
}
