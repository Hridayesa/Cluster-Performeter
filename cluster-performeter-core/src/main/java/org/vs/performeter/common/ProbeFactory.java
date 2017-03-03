package org.vs.performeter.common;

import java.time.LocalDateTime;

/**
 * Created by bubnovvy on 03.03.2017.
 */
public interface ProbeFactory<T extends  Probe> {
    T create();

    void setTimestamp(String columnName, LocalDateTime localDateTime);

    void setNull(String columnName, int type);

    void setObject(String columnName, Object object, int columnType);
}
