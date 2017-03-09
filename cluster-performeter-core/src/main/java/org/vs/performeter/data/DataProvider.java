package org.vs.performeter.data;

/**
 * Created by Denis Karpov on 21.02.2017.
 */
public interface DataProvider<D> {
    default void open(int instanceId) {};
    default void close() {};
    D nextData();

    void start();

    void stop();
}
