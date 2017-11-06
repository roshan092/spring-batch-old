package com.roshan092.springbatchdemo.service;

import com.roshan092.springbatchdemo.domain.DemoBatchOutput;
import org.springframework.batch.core.ItemWriteListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DemoWriterListener implements ItemWriteListener<DemoBatchOutput> {
    @Override
    public void beforeWrite(List<? extends DemoBatchOutput> demoBatchOutputs) {
    }

    @Override
    public void afterWrite(List<? extends DemoBatchOutput> demoBatchOutputs) {
        System.out.println("After write ===============> " + demoBatchOutputs);
    }

    @Override
    public void onWriteError(Exception exception, List<? extends DemoBatchOutput> items) {

    }
}
