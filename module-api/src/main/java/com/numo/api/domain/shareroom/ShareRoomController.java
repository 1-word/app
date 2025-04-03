package com.numo.api.domain.shareroom;

import com.numo.api.domain.shareroom.dto.MyShareRoomListDto;
import com.numo.api.domain.shareroom.dto.ShareRoomListDto;
import com.numo.api.domain.shareroom.service.ShareRoomService;
import com.numo.api.global.comm.page.PageRequestDto;
import com.numo.api.global.comm.page.PageResponse;
import com.numo.api.security.service.UserDetailsImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "쉐어룸")
@RestController
@RequiredArgsConstructor
@RequestMapping("/share-rooms")
public class ShareRoomController {
    private final ShareRoomService shareRoomService;

    @Operation(summary = "쉐어룸 공개")
    @PostMapping("/wordbook/{wordBookId}")
    public ResponseEntity<Long> saveShareRoom(@AuthenticationPrincipal UserDetailsImpl user,
                                          @PathVariable("wordBookId") Long wordBookId) {
        return ResponseEntity.ok(shareRoomService.saveShareRoom(user.getUserId(), wordBookId));
    }

    @Operation(summary = "모두의 단어장 리스트 출력)")
    @GetMapping
    public ResponseEntity<PageResponse<ShareRoomListDto>> getShareRooms(PageRequestDto pageDto) {
        return ResponseEntity.ok(shareRoomService.getShareRooms(pageDto));
    }

    @Operation(summary = "내가 공유한 단어장 출력")
    @GetMapping("/my")
    public ResponseEntity<List<MyShareRoomListDto>> getMyShareRooms(@AuthenticationPrincipal UserDetailsImpl user) {
        return ResponseEntity.ok(shareRoomService.getMyShareRooms(user.getUserId()));
    }

    @Operation(summary = "단어장 공유 취소")
    @DeleteMapping("{wordBookId}")
    public ResponseEntity<Void> deleteShareRoom(@AuthenticationPrincipal UserDetailsImpl user,
                                                @PathVariable("wordBookId") Long wordBookId) {
        shareRoomService.deleteShareRoom(user.getUserId(), wordBookId);
        return ResponseEntity.noContent().build();
    }
}
