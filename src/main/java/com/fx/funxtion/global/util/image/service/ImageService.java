package com.fx.funxtion.global.util.image.service;

import io.awspring.cloud.s3.ObjectMetadata;
import io.awspring.cloud.s3.S3Resource;
import io.awspring.cloud.s3.S3Template;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ImageService {
    private final S3Template s3Template; // s3

    @Value("${spring.cloud.aws.s3.bucket}")
    private String bucketName;

    @Transactional
    public String uploadImage(MultipartFile image) throws IOException {
        String originalFilename = image.getOriginalFilename(); // 클라이언트가 전송한 파일 이름
        String extension = StringUtils.getFilenameExtension(originalFilename); // 파일 확장자

        String uuidImageName = UUID.randomUUID().toString() + "." + extension; // 파일 이름 중복 방지

        // S3에 파일 업로드
        S3Resource s3Resource = s3Template.upload(bucketName, uuidImageName, image.getInputStream(), ObjectMetadata.builder().contentType(extension).build());

        // 업로드 된 썸네일 url을 entity 필드에 삽입
        String imageUrl = s3Resource.getURL().toString();

        return imageUrl;
    }
}