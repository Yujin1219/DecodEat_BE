package com.DecodEat.domain.report.repository;

import com.DecodEat.domain.report.entity.NutritionReport;
import com.DecodEat.domain.report.entity.ReportRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NutritionReportRepository extends JpaRepository<NutritionReport, Long> {
}
