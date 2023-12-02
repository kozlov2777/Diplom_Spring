package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TableStatusDto {
    private Long id;
    private String tableStatusName;
}
