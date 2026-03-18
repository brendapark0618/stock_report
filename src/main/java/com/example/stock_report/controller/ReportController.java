package com.example.stock_report.controller;

import java.io.IOException;

import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.stock_report.data.common.Response;
import com.example.stock_report.data.dto.AddReportRequest;
import com.example.stock_report.security.CurrentUser;
import com.example.stock_report.service.ReportService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/report_list")
@RequiredArgsConstructor
public class ReportController {

    private final ReportService reportService;

    @GetMapping("/{user}")
    public Response getReportList(@AuthenticationPrincipal CurrentUser currentUser) {
        return reportService.getReportList(currentUser.getUsername());
    }

    @PostMapping("/add_pdf")
    public Response updateReport(@RequestBody AddReportRequest request) {
        System.out.println("🚀 FastAPI로부터 받은 데이터: " + request);
        return reportService.updateReport(
                request.getUser(),
                request.getFilename(),
                request.getType(),
                request.getStoredPath());
    }

    @GetMapping("/{user}/{id}/file")
    public ResponseEntity<Resource> getReportFile(
            @AuthenticationPrincipal CurrentUser currentUser,
            @PathVariable Long id) throws IOException, Exception {
        return reportService.getReportFile(currentUser.getUsername(), id);
    }
}