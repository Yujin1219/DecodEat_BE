package com.DecodEat.domain.products.scheduler;

import com.DecodEat.domain.products.entity.DecodeStatus;
import com.DecodEat.domain.products.repository.ProductRepository;
import jakarta.transaction.TransactionScoped;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class ProductCleanupScheduler {
    private final ProductRepository productRepository;

    @Scheduled(cron = "* * 3 * * *") // 초 분 시 일 월 요일
    @Transactional
    public void cleanupFailedAndCanceledProducts(){

        List<DecodeStatus> targetStatuses = Arrays.asList(
                DecodeStatus.FAILED,
                DecodeStatus.CANCELLED);

        productRepository.deleteByDecodeStatusIn(targetStatuses);
        log.atInfo().log("Product Cleanup Scheduler: Deleted {} products with decode status in {}", targetStatuses.size(), targetStatuses);
    }
}
