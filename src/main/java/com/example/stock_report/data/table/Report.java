package com.example.stock_report.data.table;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "report")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Report {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user", nullable = false, length = 50)
    private String user;

    @Column(nullable = false, length = 255)
    private String filename;

    @Column(nullable = false)
    private LocalDateTime date;

    @Column(nullable = false, length = 20)
    private String type;

    @Column(name = "stored_path", nullable = false, length = 500)
    private String storedPath; 

    @PrePersist
    protected void onCreate() { // 엔티티가 처음 저장될 때 실행되는 메서드
        if (this.date == null) {
            this.date = LocalDateTime.now();
        }
    }
}