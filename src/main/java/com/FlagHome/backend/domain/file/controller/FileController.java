package com.FlagHome.backend.domain.file.controller;

import com.FlagHome.backend.domain.file.service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/file")
@RequiredArgsConstructor
public class FileController {
    private final FileService fileService;

    @PostMapping
    public ResponseEntity<List<String>> uploadFile(@RequestPart("fileList") List<MultipartFile> fileList) {
        return ResponseEntity.status(HttpStatus.OK).body(fileService.upload(fileList));
    }

    @DeleteMapping("/{fileName}")
    public ResponseEntity<Void> deleteFile(@PathVariable(name = "fileName") String fileName) {
        fileService.delete(fileName);
        return ResponseEntity.noContent().build();
    }
}
