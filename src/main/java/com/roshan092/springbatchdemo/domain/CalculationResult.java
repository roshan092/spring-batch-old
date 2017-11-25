package com.roshan092.springbatchdemo.domain;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class CalculationResult {
    private Integer id;
    private Integer value;
}
