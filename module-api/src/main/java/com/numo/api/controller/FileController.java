package com.numo.api.controller;

import com.numo.api.security.service.UserDetailsImpl;
import com.numo.api.service.file.FileService;
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

    @Operation(summary = "파일 업로드")
    @PostMapping(value = "/upload/{middlePath}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<List<String>> upload(@AuthenticationPrincipal UserDetailsImpl user,
                                               @PathVariable("middlePath") String middlePath,
                                               @RequestPart("files") ArrayList<MultipartFile> files) {
        return ResponseEntity.ok(fileService.uploadAndSave(user.getUserId(), middlePath, files));
    }

    @Operation(summary = "파일 다운로드")
    @GetMapping(value = "/{fileId}")
    public ResponseEntity<Resource> download(@AuthenticationPrincipal UserDetailsImpl user,
                                             @PathVariable("fileId") String fileId) {
        return ResponseEntity.ok(fileService.download(user.getUserId(), fileId));
    }

    @Operation(summary = "썸네일 이미지 업로드", description = "로그인 없이 업로드 가능, 파일만 저장(파일 데이터베이스에 저장X)")
    @PostMapping(value = "/upload/thumbnail", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> upload(@RequestPart("files") MultipartFile file) {
        return ResponseEntity.ok(fileService.uploadImage("thumbnail", file));
    }

    @Operation(summary = "이미지 업로드", description = "로그인 필요, 파일만 저장(파일 데이터베이스에 저장X)")
    @PostMapping(value = "/upload/images/{middlePath}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> uploadImages(@PathVariable("middlePath") String middlePath,
                                               @RequestPart("files") MultipartFile file) {
        return ResponseEntity.ok(fileService.uploadImage("images/" + middlePath, file));
    }

    @Operation(summary = "이미지 파일 다운로드")
    @GetMapping("/images/{fileId}")
    public Resource imageDownload(@PathVariable("fileId") String fileId) {
        return fileService.download(fileId);
    }

    @Operation(summary = "파일 삭제")
    @DeleteMapping("{fileId}")
    public ResponseEntity<Void> delete(@AuthenticationPrincipal UserDetailsImpl user,
                                       @PathVariable("fileId") String fileId) {
        fileService.delete(user.getUserId(), fileId);
        return ResponseEntity.noContent().build();
    }
}
