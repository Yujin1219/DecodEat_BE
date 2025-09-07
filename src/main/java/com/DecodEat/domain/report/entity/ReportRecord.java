package com.DecodEat.domain.report.entity;

import com.DecodEat.domain.products.entity.Product;
import com.DecodEat.global.common.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@SuperBuilder // 자식 클래스에서 부모 클래스 필드까지 빌드(컴파일 시점에) <-> @Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED) // 서비스 코드 등 다른 곳에서 의미 없는 빈 객체를 실수로 생성하는 것 예방
@Table(name = "report_records")
@Inheritance(strategy = InheritanceType.JOINED) // JOINED 사용
@DiscriminatorColumn(name = "REPORT_TYPE") // 자식 엔티티를 구분할 컬럼 (JPA가 생성)
public abstract class ReportRecord extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(nullable = false)
    private Long reporterId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Schema(name = "처리 상태", example = "IN_PROGRESS")
    private ReportStatus reportStatus;
}
