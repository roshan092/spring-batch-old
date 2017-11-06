package com.roshan092.springbatchdemo.service;

import com.roshan092.springbatchdemo.domain.DemoBatchInput;
import org.springframework.batch.core.ItemReadListener;
import org.springframework.stereotype.Component;

@Component
public class DemoReaderListener implements ItemReadListener<DemoBatchInput> {
    @Override
    public void beforeRead() {
    }

    @Override
    public void afterRead(DemoBatchInput demoBatchInput) {
        System.out.println("After reading--------------> " + demoBatchInput.toString());
    }

    @Override
    public void onReadError(Exception ex) {
    }
}
