package com.roshan092.springbatchdemo.service;

import com.roshan092.springbatchdemo.domain.DemoBatchInput;
import com.roshan092.springbatchdemo.domain.DemoBatchOutput;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Service;

@Service
public class DemoItemProcessor implements ItemProcessor<DemoBatchInput, DemoBatchOutput> {
    @Override
    public DemoBatchOutput process(DemoBatchInput demoBatchInput) throws Exception {
        Thread.sleep(5000L);
        return DemoBatchOutput.builder()
                .name(demoBatchInput.getFirstName().toUpperCase()
                        + " " + demoBatchInput.getLastName().toUpperCase())
                .build();
    }
}
