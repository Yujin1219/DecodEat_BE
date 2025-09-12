package com.DecodEat.domain.report.repository;

import com.DecodEat.domain.report.entity.ReportRecord;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ReportRecordRepository extends JpaRepository<ReportRecord, Long> {

    // 영양정보 전체 조회
    @Query(value = "SELECT r FROM ReportRecord r " +
            "JOIN FETCH r.product p " +
            "LEFT JOIN FETCH p.productNutrition",
            countQuery = "SELECT count(r) FROM ReportRecord r")
    Page<ReportRecord> findAllWithDetails(Pageable pageable);

    // 단일 조회
    @Query("SELECT r FROM ReportRecord r " +
            "JOIN FETCH r.product p " +
            "LEFT JOIN FETCH p.productNutrition " +
            "WHERE r.id = :id")
    Optional<ReportRecord> findByIdWithDetails(Long id);
}
