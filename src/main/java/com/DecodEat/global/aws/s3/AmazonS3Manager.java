package com.DecodEat.global.aws.s3;

import com.DecodEat.global.config.AmazonConfig;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URL;

@Slf4j
@Component
@RequiredArgsConstructor
public class AmazonS3Manager{

    private final AmazonS3 amazonS3;

    private final AmazonConfig amazonConfig;

    public String uploadFile(String keyName, MultipartFile file){
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(file.getSize());
        try {
            amazonS3.putObject(new PutObjectRequest(amazonConfig.getBucket(), keyName, file.getInputStream(), metadata));
        } catch (IOException e){
            log.error("error at AmazonS3Manager uploadFile : {}", (Object) e.getStackTrace());
        }

        return amazonS3.getUrl(amazonConfig.getBucket(), keyName).toString();
    }

    public void deleteFile(String keyName){
        try {
            amazonS3.deleteObject(new DeleteObjectRequest(amazonConfig.getBucket(), keyName));
            log.info("S3 파일 삭제 성공: key={}", keyName);
        } catch (Exception e) {
            log.error("S3 파일 삭제 중 에러 발생: key={}", keyName, e);
        }

    }

    public String getKeyFromUrl(String fileUrl) {
        try {
            URL url = new URL(fileUrl);
            // URL 경로에서 맨 앞의 '/'를 제외한 부분이 key가 됨
            return url.getPath().substring(1);
        } catch (Exception e) {
            log.error("URL로부터 S3 key 추출 중 에러 발생: url={}", fileUrl, e);
            throw new RuntimeException("URL parsing error", e);
        }
    }
}
