package com.example.stock_report.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.stock_report.data.table.Report;

public interface ReportRepository extends JpaRepository<Report, Long> {

    // List<Report> findByUser(String user); // user로 보고서 리스트 조회 <- 정렬이 없는게 아쉬움...

    List<Report> findByUserOrderByDateDesc(String user); // user로 보고서 리스트 조회 + 날짜 내림차순 정렬

    Optional<Report> findByIdAndUser(Long id, String user); // id와 user로 보고서 조회

    // List<Report> findByUserAndTypeOrderByDateDesc(String user, String type); // 나중에 리포트 타입별로 조회할 때 필요할 수도 있을듯
}
