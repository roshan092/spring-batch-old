package com.roshan092.springbatchdemo.service;

import com.roshan092.springbatchdemo.domain.DemoBatchInput;
import com.roshan092.springbatchdemo.domain.DemoBatchOutput;
import org.springframework.batch.core.ItemProcessListener;
import org.springframework.stereotype.Component;

@Component
public class DemoProcessorListener
        implements ItemProcessListener<DemoBatchInput, DemoBatchOutput> {
    @Override
    public void beforeProcess(DemoBatchInput demoBatchInput) {
        System.out.println("Before process ===============> " + demoBatchInput);
    }

    @Override
    public void afterProcess(DemoBatchInput demoBatchInput, DemoBatchOutput demoBatchOutput) {
        System.out.println("After process ===============> " + demoBatchInput);
    }

    @Override
    public void onProcessError(DemoBatchInput item, Exception e) {

    }
}
