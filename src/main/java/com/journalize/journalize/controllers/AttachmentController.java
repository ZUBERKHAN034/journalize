package com.journalize.journalize.controllers;

import java.io.IOException;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.MediaType;

import com.journalize.journalize.dto.ApiResponse;
import com.journalize.journalize.dto.attachment.FileIdResponse;
import com.journalize.journalize.dto.attachment.PresignedUrlResponse;
import com.journalize.journalize.services.AttachmentService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/attachment")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class AttachmentController {

    private final AttachmentService attachmentService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<?>> upload(@RequestParam("file") MultipartFile file) throws IOException {
        ApiResponse<FileIdResponse> response = attachmentService.uploadFile(file);
        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<?>> getUrl(@PathVariable String id) {
        ApiResponse<PresignedUrlResponse> response = attachmentService.getAccessUrl(id);
        return ResponseEntity.ok().body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<?>> delete(@PathVariable String id) {
        ApiResponse<Void> response = attachmentService.deleteFile(id);
        return ResponseEntity.ok().body(response);
    }
}