package com.roshan092.springbatchdemo.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DemoBatchOutput {
//    private String name;
    private Integer id;
    private Integer value;
}
