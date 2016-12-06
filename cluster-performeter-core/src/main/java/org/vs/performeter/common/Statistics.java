package org.vs.performeter.common;

import java.io.Serializable;

/**
 * Created by dekar on 05.12.2016.
 */
public interface Statistics extends Serializable{
    public Long getCount();
    public Long getMilliseconds();
}
