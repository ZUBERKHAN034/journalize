package com.journalize.journalize.services;

import java.io.IOException;
import java.time.Duration;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.journalize.journalize.dto.ApiResponse;
import com.journalize.journalize.dto.attachment.FileIdResponse;
import com.journalize.journalize.dto.attachment.PresignedUrlResponse;
import com.journalize.journalize.exceptions.BadRequestException;
import com.journalize.journalize.utils.S3Helper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AttachmentService {

    private final long MAX_FILE_SIZE = 10L * 1024 * 1024; // 10MB Size limit 
    private final List<String> ALLOWED_CONTENT_TYPES = List.of("image/jpeg", "image/png");
    private final Duration DEFAULT_URL_EXPIRY = Duration.ofMinutes(15);

    private final S3Helper s3Helper;

    public ApiResponse<FileIdResponse> uploadFile(MultipartFile file) throws IOException {
        String contentType = file.getContentType();

        if (file == null || file.isEmpty()) {
            throw new BadRequestException("File must not be empty");
        }

        if (file.getSize() > MAX_FILE_SIZE) {
            throw new BadRequestException("File size exceeds 10MB limit");
        }

        if (contentType == null || !ALLOWED_CONTENT_TYPES.contains(contentType)) {
            throw new BadRequestException("Unsupported file type: " + contentType);
        }

        String id = s3Helper.upload(file);
        FileIdResponse fileId = FileIdResponse.builder().fileId(id).build();

        return ApiResponse.success("File uploaded successfully", fileId);
    }

    public ApiResponse<PresignedUrlResponse> getAccessUrl(String id) {
        String url = s3Helper.getPresignedUrl(id, DEFAULT_URL_EXPIRY);
        PresignedUrlResponse presignedUrl = PresignedUrlResponse.builder().presignedUrl(url).build();
        return ApiResponse.success("File fetched successfully", presignedUrl);
    }

    public ApiResponse<Void> deleteFile(String id) {
        s3Helper.delete(id);
        return ApiResponse.success("File deleted successfully");
    }
}