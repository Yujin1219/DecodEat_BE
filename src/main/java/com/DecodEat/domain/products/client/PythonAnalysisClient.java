package com.DecodEat.domain.products.client;

import com.DecodEat.domain.products.dto.request.AnalysisRequestDto;
import com.DecodEat.domain.products.dto.response.AnalysisResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.Duration;

@Component
@RequiredArgsConstructor
@Slf4j
public class PythonAnalysisClient {

    private final WebClient webClient;

    @Value("${python.server.url:http://3.37.218.215:8000/}")
    private String pythonServerUrl;

    public Mono<AnalysisResponseDto> analyzeProduct(AnalysisRequestDto request) {
        log.info("Sending analysis request to Python server: {}", pythonServerUrl);
        
        return webClient.post()
                .uri(pythonServerUrl + "/api/v1/analyze")
                .bodyValue(request)
                .retrieve()
                .bodyToMono(AnalysisResponseDto.class)
                .timeout(Duration.ofMinutes(2))
                .doOnSuccess(response -> log.info("Analysis completed with status: {}", response.getDecodeStatus()))
                .doOnError(error -> log.error("Analysis request failed: {}", error.getMessage()));
    }
}