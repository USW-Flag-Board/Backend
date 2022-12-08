package com.FlagHome.backend.v1.post.file.controller;

import com.FlagHome.backend.v1.util.FileUploadService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/files")
@RequiredArgsConstructor
public class FileController {
    private final FileUploadService fileUploadService;


}
