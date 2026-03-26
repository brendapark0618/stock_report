package com.example.stock_report.data.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 🚀 FastAPI로부터 JSON 데이터를 받기 위한 전용 DTO
 * 필드명은 FastAPI의 JSON Key 값과 정확히 일치해야 합니다.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AddReportRequest {
    @JsonProperty("user")
    private String user;

    @JsonProperty("filename")
    private String filename;

    @JsonProperty("type")
    private String type;

    @JsonProperty("storedPath")
    private String storedPath;
}