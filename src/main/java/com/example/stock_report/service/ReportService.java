package com.example.stock_report.service;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.example.stock_report.config.ReportStorageProperties;
import com.example.stock_report.data.common.Response;
import com.example.stock_report.data.dto.ReportFilenameDto;
import com.example.stock_report.data.dto.ReportListDto;
import com.example.stock_report.data.table.Report;
import com.example.stock_report.repository.ReportRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReportService {

    private final ReportRepository reportRepository;
    private final ReportStorageProperties reportStorageProperties;

    private static final DateTimeFormatter DATE_FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public Response getReportList(String user) { // GET: user로 보고서 리스트 조회

        // user로 보고서 리스트 조회 + 날짜 내림차순 정렬
        List<Report> reportList = reportRepository.findByUserOrderByDateDesc(user);

        // Report 엔티티를 ReportDto로 변환하여 리스트에 담기
        List<ReportFilenameDto> reportDtoList = reportList.stream()
                .map(report -> new ReportFilenameDto(
                        report.getId(),
                        report.getUser(),
                        report.getFilename(),
                        report.getDate().format(DATE_FORMATTER),
                        report.getType()
                ))
                .collect(Collectors.toList());

        // ReportListDto 생성
        ReportListDto reportListDto = new ReportListDto(user, reportDtoList);

        Response response = new Response();
        response.setResult(Response.SUCCESS);
        response.setBody(reportListDto);

        return response;
    }

    public Response updateReport(String user, String filename, String type, String storedPath) { // POST: 보고서 정보 저장

        Report report = Report.builder()
                .user(user)
                .filename(filename)
                .type(type)
                .storedPath(storedPath)
                .build();

        reportRepository.save(report); // DB에 보고서 정보 저장

        Response response = new Response();
        response.setResult(Response.SUCCESS);
        return response;
    }   

    public ResponseEntity<Resource> getReportFile(String user, Long id) throws Exception { // GET: id와 user로 보고서 조회 + 파일 다운로드, streaming 방식으로 파일 전송

        // id와 user로 보고서 조회
        Report report = reportRepository.findByIdAndUser(id, user)
                .orElseThrow(() -> new IllegalArgumentException("REPORT_NOT_FOUND"));

        Path filePath = Paths.get(report.getStoredPath()).normalize();

        if (!Files.exists(filePath) || !Files.isRegularFile(filePath)) {
            throw new IllegalArgumentException("REPORT_FILE_NOT_FOUND");
        }

        Resource resource = new FileSystemResource(filePath);

        String contentType = Files.probeContentType(filePath);
        if (contentType == null) {
            contentType = MediaType.APPLICATION_PDF_VALUE;
        }

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        ContentDisposition.inline()
                                .filename(report.getFilename())
                                .build()
                                .toString())
                .body(resource);
    }

    private Path resolveReportPath(Report report) throws Exception {

        Path root = Paths.get(reportStorageProperties.getStorageRoot()).normalize().toAbsolutePath();
        Path relative = Paths.get(report.getStoredPath()).normalize();
        Path fullPath = root.resolve(relative).normalize();

        if(!fullPath.startsWith(root)) {
            throw new IllegalArgumentException("INVALID_FILE_PATH");
        }

        return fullPath;
    }
}
