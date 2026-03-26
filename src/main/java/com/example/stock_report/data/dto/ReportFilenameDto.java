package com.example.stock_report.data.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReportFilenameDto {
    
    private Long id;
    private String user;
    private String filename;
    private String date;
    private String type; // AI 리포트, 반도체 리포트 인지
}