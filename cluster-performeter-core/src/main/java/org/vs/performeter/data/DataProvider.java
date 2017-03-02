package org.vs.performeter.data;

/**
 * Created by Denis Karpov on 21.02.2017.
 */
public interface DataProvider<D> {
    D nextData();
}
