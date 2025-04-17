package com.numo.api.domain.shareroom.service;

import com.numo.api.domain.shareroom.dto.MyShareRoomListDto;
import com.numo.api.domain.shareroom.dto.ShareRoomListDto;
import com.numo.api.domain.shareroom.repository.ShareRoomQueryRepository;
import com.numo.api.domain.shareroom.repository.ShareRoomRepository;
import com.numo.api.domain.wordbook.service.WordBookCacheService;
import com.numo.api.global.comm.exception.CustomException;
import com.numo.api.global.comm.exception.ErrorCode;
import com.numo.api.global.comm.page.PageRequestDto;
import com.numo.api.global.comm.page.PageResponse;
import com.numo.domain.shareroom.ShareRoom;
import com.numo.domain.wordbook.WordBook;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ShareRoomService {
    private final WordBookCacheService wordBookCacheService;
    private final ShareRoomRepository shareRoomRepository;
    private final ShareRoomQueryRepository shareRoomQueryRepository;

    @Transactional
    public Long saveShareRoom(Long userId, Long wordBookId) {
        if (shareRoomRepository.existsByWordBook_Id(wordBookId)) {
            throw new CustomException(ErrorCode.SHARE_ROOM_EXISTS);
        }

        WordBook wordBook = wordBookCacheService.findWordBook(wordBookId);

        if (!wordBook.isOwner(userId)) {
            throw new CustomException(ErrorCode.NOT_OWNER);
        }

        ShareRoom shareRoom = ShareRoom.builder()
                .wordBook(wordBook)
                .build();

        Long shareRoomId = shareRoomRepository.save(shareRoom).getId();

        wordBook.startSharing();
        return shareRoomId;
    }

    public PageResponse<ShareRoomListDto> getShareRooms(PageRequestDto pageDto) {
        Slice<ShareRoomListDto> shareRooms = shareRoomQueryRepository.getShareRooms(pageDto.lastId(), pageDto.to());
        List<ShareRoomListDto> content = shareRooms.getContent();
        Long lastId = null;
        if (!content.isEmpty()) {
            lastId = content.get(content.size() - 1).id();
        }
        return new PageResponse<>(shareRooms, lastId);
    }

    public List<MyShareRoomListDto> getMyShareRooms(Long userId) {
        return shareRoomQueryRepository.getMyShareRooms(userId);
    }

    @Transactional
    public void deleteShareRoom(Long userId, Long shareRoomId) {
        ShareRoom shareRoom = getShareRoom(shareRoomId);
        if (!shareRoom.isOwner(userId)) {
            throw new CustomException(ErrorCode.NOT_OWNER);
        }
        shareRoomRepository.delete(shareRoom);
        WordBook wordBook = shareRoom.getWordBook();
        wordBook.cancelSharing();
    }

    public ShareRoom getShareRoom(Long id) {
        return shareRoomRepository.findShareRoomByIdWithWordBook(id);
    }
}
