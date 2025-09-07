package com.DecodEat.domain.report.entity;


import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;


@Entity
@Table(name = "image_reports")
@Getter
@DiscriminatorValue("INAPPROPRIATE_IMAGE")
public class ImageReport extends ReportRecord {

    @Column(name = "image_url")
    private String imageUrl;

}
