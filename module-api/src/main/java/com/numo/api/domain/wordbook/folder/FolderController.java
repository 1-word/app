package com.numo.api.domain.wordbook.folder;

import com.numo.api.domain.wordbook.folder.dto.FolderRequestDto;
import com.numo.api.domain.wordbook.folder.dto.FolderResponseDto;
import com.numo.api.domain.wordbook.folder.dto.read.FolderListReadResponseDto;
import com.numo.api.domain.wordbook.folder.service.FolderService;
import com.numo.api.security.service.UserDetailsImpl;
import com.numo.domain.wordbook.folder.dto.FolderUpdateDto;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/folders")
@RestController
@RequiredArgsConstructor
public class FolderController {
    private final FolderService folderService;

    @Operation(description = "폴더명 리스트를 가져온다.")
    @GetMapping
    public ResponseEntity<List<FolderListReadResponseDto>> getFolderName(@AuthenticationPrincipal UserDetailsImpl user){
        return ResponseEntity.ok(folderService.getFolders(user.getUserId(), null));
    }

    @Operation(description = "공유 폴더 리스트를 가져온다.")
    @GetMapping("/share")
    public ResponseEntity<List<FolderListReadResponseDto>> getShareFolder(@AuthenticationPrincipal UserDetailsImpl user){
        return ResponseEntity.ok(folderService.getShareFolders(user.getUserId()));
    }

    @Operation(summary = "단어 개수 조회", description = "폴더 안의 단어 개수를 조회한다")
    @GetMapping("/{folderId}")
    public ResponseEntity<Long> getWordCountInFolder(@AuthenticationPrincipal UserDetailsImpl user,
                                                     @PathVariable("folderId") Long folderId,
                                                     @RequestParam(value = "memorization", required = false) Boolean memorization) {
        return ResponseEntity.ok(folderService.getWordCountInFolder(user.getUserId(), folderId, memorization));
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
