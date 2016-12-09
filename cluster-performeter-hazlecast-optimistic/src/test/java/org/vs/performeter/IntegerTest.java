package org.vs.performeter;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

/**
 * Created by Denis Karpov on 09.12.2016.
 */
public class IntegerTest {
    @Rule
    public ExpectedException thrown= ExpectedException.none();

    @Test
    public void nullTest() throws Exception {
        thrown.expect(NullPointerException.class);
        Integer i = null;
        Integer a = i + 1; // NullPointer

        Assert.assertEquals(1,a.intValue());

    }
}
