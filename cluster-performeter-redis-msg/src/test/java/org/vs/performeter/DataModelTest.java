package org.vs.performeter;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTestContextBootstrapper;
import org.springframework.test.context.BootstrapWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.vs.performeter.data.DataProvider;
import org.vs.performeter.data.dummy.Msg;
import org.vs.performeter.tester.TesterMain;

import javax.annotation.Resource;

/**
 * Created by Denis Karpov on 21.02.2017.
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {TesterMain.class})
@BootstrapWith(SpringBootTestContextBootstrapper.class)
public class DataModelTest {
    @Resource(name = "dummyMsgGenDataProvider")
    private DataProvider<Msg> dataProvider;

    @Test
    public void testBlob() throws Exception {
        Msg msg = dataProvider.nextData();
        Assert.assertEquals(1009, msg.getBbb().length());
        System.out.println(msg.getBbb());
    }
}
