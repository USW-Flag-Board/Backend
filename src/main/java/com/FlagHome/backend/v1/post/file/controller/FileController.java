package com.FlagHome.backend.v1.post.file.controller;

import com.FlagHome.backend.global.util.FileUploadService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/")
@RequiredArgsConstructor
public class FileController {
    private final FileUploadService fileUploadService;
}
