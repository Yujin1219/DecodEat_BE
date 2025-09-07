package com.DecodEat.domain.report.entity;


import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;


@Entity
@Table(name = "image_reports")
@Getter
@DiscriminatorValue("INAPPROPRIATE_IMAGE")
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ImageReport extends ReportRecord {

    @Column(name = "image_url")
    private String imageUrl;

}
