package com.FlagHome.backend.v1.post.file.controller;

import com.FlagHome.backend.v1.util.FileUploadService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/")
@RequiredArgsConstructor
public class FileController {
    private final FileUploadService fileUploadService;
}
