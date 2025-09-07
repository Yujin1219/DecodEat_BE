package com.DecodEat.domain.report.repository;

import com.DecodEat.domain.report.entity.ImageReport;
import com.DecodEat.domain.report.entity.NutritionReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.awt.*;

@Repository
public interface ImageReportRepository extends JpaRepository<ImageReport, Long> {
}
