package com.FlagHome.backend.domain.file.controller;

import com.FlagHome.backend.domain.file.service.FileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Tag(name = "file", description = "파일 API")
@RestController
@RequestMapping("/api/file")
@RequiredArgsConstructor
public class FileController {
    private final FileService fileService;

    @Tag(name = "file")
    @Operation(summary = "AWS S3에 파일 업로드")
    @ApiResponse(responseCode = "200", description = "파일 업로드에 성공하였습니다. (파일의 URL을 리턴해줍니다.)")
    @PostMapping
    public ResponseEntity<List<String>> uploadFile(@RequestPart("fileList") List<MultipartFile> fileList) {
        return ResponseEntity.status(HttpStatus.OK).body(fileService.upload(fileList));
    }

    @Tag(name = "file")
    @Operation(summary = "AWS S3에서 파일 삭제")
    @ApiResponse(responseCode = "204", description = "파일 삭제에 성공하였습니다.")
    @DeleteMapping("/{fileName}")
    public ResponseEntity<Void> deleteFile(@PathVariable(name = "fileName") String fileName) {
        fileService.delete(fileName);
        return ResponseEntity.noContent().build();
    }
}
