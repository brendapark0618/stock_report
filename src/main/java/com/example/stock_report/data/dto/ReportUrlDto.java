package com.example.stock_report.data.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReportUrlDto {
    
    private Long id;
    private String user;
    private String fileUrl; // 파일이 저장된 경로
}