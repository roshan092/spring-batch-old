package com.roshan092.springbatchdemo.service;

import com.roshan092.springbatchdemo.domain.CalculationResult;
import com.roshan092.springbatchdemo.domain.DemoBatchInput;
import com.roshan092.springbatchdemo.domain.DemoBatchOutput;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class DemoItemProcessor implements ItemProcessor<DemoBatchInput, DemoBatchOutput> {

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public DemoBatchOutput process(DemoBatchInput demoBatchInput) throws Exception {
        Thread.sleep(1000L);
        String url
                = String.format("http://localhost:9090/calculate?id=%d",
                demoBatchInput.getId());
        ResponseEntity<CalculationResult> responseEntity
                = restTemplate.getForEntity(url, CalculationResult.class);
        return DemoBatchOutput.builder()
//                .name(demoBatchInput.getFirstName().toUpperCase()
//                        + " " + demoBatchInput.getLastName().toUpperCase())
                .id(responseEntity.getBody().getId())
                .value(responseEntity.getBody().getValue())
                .build();
    }
}
