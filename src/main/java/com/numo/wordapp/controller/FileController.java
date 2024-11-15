package com.numo.wordapp.controller;

import com.numo.wordapp.security.service.UserDetailsImpl;
import com.numo.wordapp.service.file.FileService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@RequestMapping("/files/")
@RestController
@RequiredArgsConstructor
public class FileController {
    private final FileService fileService;

    @Operation(description = "파일 업로드")
    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<List<String>> upload(@AuthenticationPrincipal UserDetailsImpl user,
                                               @RequestPart("files") ArrayList<MultipartFile> files) {
        return ResponseEntity.ok(fileService.upload(user.getUserId(), files));
    }

    @Operation(description = "파일 다운로드")
    @GetMapping(value = "/{fileId}")
    public ResponseEntity<Resource> download(@AuthenticationPrincipal UserDetailsImpl user,
                                             @PathVariable("fileId") String fileId) {
        return ResponseEntity.ok(fileService.download(user.getUserId(), fileId));
    }

    @Operation(description = "이미지 파일 다운로드")
    @GetMapping("/images/{fileId}")
    public Resource imageDownload(@PathVariable("fileId") String fileId) {
        return fileService.download(fileId);
    }

    @Operation(description = "파일 삭제")
    @DeleteMapping("{fileId}")
    public ResponseEntity<Void> delete(@AuthenticationPrincipal UserDetailsImpl user,
                                       @PathVariable("fileId") String fileId) {
        fileService.delete(user.getUserId(), fileId);
        return ResponseEntity.noContent().build();
    }
}
