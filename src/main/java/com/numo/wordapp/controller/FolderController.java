package com.numo.wordapp.controller;

import com.numo.domain.word.folder.dto.FolderUpdateDto;
import com.numo.wordapp.dto.folder.FolderInWordCountDto;
import com.numo.wordapp.dto.folder.FolderListReadResponseDto;
import com.numo.wordapp.dto.folder.FolderRequestDto;
import com.numo.wordapp.dto.folder.FolderResponseDto;
import com.numo.wordapp.security.service.UserDetailsImpl;
import com.numo.wordapp.service.word.FolderService;
import com.numo.wordapp.service.word.WordService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RequestMapping("/folders")
@RestController
@RequiredArgsConstructor
public class FolderController {
    private final WordService wordService;
    private final FolderService folderService;

    @Operation(description = "폴더명 리스트를 가져온다.")
    @GetMapping
    public ResponseEntity<List<FolderListReadResponseDto>> getFolderName(@AuthenticationPrincipal UserDetailsImpl user){
        List<FolderResponseDto> folders = folderService.getFolders(user.getUserId());
        Map<Long, FolderInWordCountDto> folderInWordCountMap = wordService.getFolderInWordCount(user.getUserId());
        return ResponseEntity.ok(folderService.getFolders(folders, folderInWordCountMap));
    }

    @Operation(description = "폴더를 생성한다.")
    @PostMapping
    public ResponseEntity<FolderResponseDto> saveFolder(@AuthenticationPrincipal UserDetailsImpl user,
                                                        @RequestBody FolderRequestDto folderDto){
        Long userId = user.getUserId();
        return ResponseEntity.ok(folderService.saveFolder(userId, folderDto));
    }

    @Operation(description = "폴더를 변경한다.")
    @PutMapping (value = "/{folderId}")
    public ResponseEntity<FolderResponseDto> updateFolder(@AuthenticationPrincipal UserDetailsImpl user,
                                                          @PathVariable("folderId") Long folderId,
                                                          @RequestBody FolderUpdateDto folderDto){
        Long userId = user.getUserId();
        return ResponseEntity.ok(folderService.updateFolder(userId, folderId, folderDto));
    }

    @Operation(description = "폴더를 삭제한다.")
    @DeleteMapping(value="/{folderId}")
    public ResponseEntity<Integer> removeFolder(@AuthenticationPrincipal UserDetailsImpl user,
                                                @PathVariable("folderId") Long folderId){
        Long userId = user.getUserId();
        folderService.removeFolder(userId, folderId);
        return ResponseEntity.noContent().build();
    }
}
