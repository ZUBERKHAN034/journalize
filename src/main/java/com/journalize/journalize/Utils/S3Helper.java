package com.journalize.journalize.utils;

import java.io.IOException;
import java.time.Duration;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;

@Component
@RequiredArgsConstructor
public class S3Helper {

    @Value("${backblaze.bucket}")
    private String bucket;

    @Value("${backblaze.main-folder}")
    private String mainFolder;

    private final S3Client s3Client;
    private final S3Presigner s3Presigner;

    public String upload(MultipartFile file) throws IOException {
        String id = UUID.randomUUID().toString();
        String fullKey = buildFullKey(id);

        PutObjectRequest request = PutObjectRequest.builder()
                .bucket(bucket)
                .key(fullKey)
                .contentType(file.getContentType())
                .build();

        s3Client.putObject(request, RequestBody.fromInputStream(file.getInputStream(), file.getSize()));
        return id;
    }

    public void delete(String id) {
        s3Client.deleteObject(DeleteObjectRequest.builder()
                .bucket(bucket)
                .key(buildFullKey(id))
                .build());
    }

    public String getPresignedUrl(String id, Duration expiry) {
        GetObjectPresignRequest presignRequest = GetObjectPresignRequest.builder()
                .signatureDuration(expiry)
                .getObjectRequest(GetObjectRequest.builder().bucket(bucket).key(buildFullKey(id)).build())
                .build();

        return s3Presigner.presignGetObject(presignRequest).url().toString();
    }

    private String buildFullKey(String id) {
        String folder = mainFolder.endsWith("/") ? mainFolder : mainFolder + "/";
        return folder + id;
    }
}