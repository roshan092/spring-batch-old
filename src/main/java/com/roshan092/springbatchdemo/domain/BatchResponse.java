package com.roshan092.springbatchdemo.domain;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class BatchResponse {
    private Long jobExecutionId;
    private Integer readCount;
    private Integer processedCount;
    private Integer writeCount;
}
