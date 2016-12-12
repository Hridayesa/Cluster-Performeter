package org.vs.performeter.analyser;

import org.springframework.stereotype.Component;
import org.vs.performeter.common.DefaultStatistics;

/**
 * Created by Denis Karpov on 06.12.2016.
 */
@Component
public class IgniteAnalyser extends AbstractAnalyser<DefaultStatistics> {

    @Override
    public void prepare() {
        //template.delete("QQQ");
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
