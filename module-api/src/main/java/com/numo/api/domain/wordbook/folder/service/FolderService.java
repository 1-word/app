package com.numo.api.domain.wordbook.folder.service;

import com.numo.api.domain.wordbook.folder.dto.FolderRequestDto;
import com.numo.api.domain.wordbook.folder.dto.FolderResponseDto;
import com.numo.api.domain.wordbook.folder.dto.read.FolderInWordCountDto;
import com.numo.api.domain.wordbook.folder.dto.read.FolderListReadResponseDto;
import com.numo.api.domain.wordbook.folder.repository.FolderRepository;
import com.numo.api.domain.wordbook.folder.repository.query.FolderQueryRepository;
import com.numo.api.global.comm.exception.CustomException;
import com.numo.api.global.comm.exception.ErrorCode;
import com.numo.domain.word.folder.Folder;
import com.numo.domain.word.folder.dto.FolderUpdateDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FolderService {

    private final FolderRepository folderRepository;
    private final FolderQueryRepository folderQueryRepository;

    /**
     * 폴더 다건 조회
     * @param userId 유저 아이디(Nullable)
     * @param folderId 폴더 아이디(Nullable)
     * @return 조회한 폴더 데이터
     */
    public List<FolderListReadResponseDto> getFolders(Long userId, Long folderId){
        List<FolderResponseDto> res = folderQueryRepository.getFoldersByUserId(userId, folderId);
        List<FolderInWordCountDto> wordCounts = folderQueryRepository.countWordInFolder(userId);
        Map<Long, FolderInWordCountDto> wordCountMap = wordCounts.stream().collect(Collectors.toMap(FolderInWordCountDto::folderId, Function.identity()));

        return getFolders(res, wordCountMap);
    }

    /**
     * 폴더 및 폴더 안의 단어 수 조회
     * @param folders 폴더
     * @param folderInWordCountMap folderId가 key로 되어있는 폴더 안의 단어 수 정보
     * @return 폴더 및 폴더 안의 단어 수 조회 데이터
     */
    public List<FolderListReadResponseDto> getFolders(List<FolderResponseDto> folders, Map<Long, FolderInWordCountDto> folderInWordCountMap) {
        List<FolderListReadResponseDto> results = folders.stream()
                .map(folderResponseDto -> new FolderListReadResponseDto(
                        folderResponseDto, getFolderInWordCount(folderResponseDto.folderId(), folderInWordCountMap))
                ).toList();
        return results;
    }

    /**
     * 폴더 안의 단어 수 조회
     * @param folderId 폴더 아이디
     * @param folderInWordCountMap folderId가 key로 되어있는 폴더 안의 단어 수 정보
     * @return 폴더 안의 단어 수, 데이터가 없으면 0을 리턴
     */
    private Long getFolderInWordCount(Long folderId, Map<Long, FolderInWordCountDto> folderInWordCountMap) {
        if (folderInWordCountMap.containsKey(folderId)) {
            return folderInWordCountMap.get(folderId).count();
        }

        return 0L;
    }

    public boolean existsFolder(Long folderId, Long userId) {
        return folderRepository.existsByFolderIdAndUser_UserId(folderId, userId);
    }

    /**
     * 폴더 저장
     * @param userId 유저 아이디
     * @param folderDto 저장할 폴더 데이터
     * @return 저장한 폴더 데이터
     */
    public FolderResponseDto saveFolder(Long userId, FolderRequestDto folderDto) {
        Folder folder = folderDto.toEntity(userId);
        return FolderResponseDto.of(folderRepository.save(folder));
    }

    /**
     * 폴더 수정
     * @param userId 유저 아이디
     * @param folderId 폴더 아이디
     * @param folderDto 수정할 폴더 데이터
     * @return 수정된 폴더 데이터
     */
    @Transactional
    public FolderResponseDto updateFolder(Long userId, Long folderId, FolderUpdateDto folderDto){
        Folder folder = folderRepository.findByFolderIdAndUserId(folderId, userId);
        folder.update(folderDto);
        return FolderResponseDto.of(folder);
    }

    /**
     * 폴더 삭제O
     * @param userId 유저 아이디
     * @param folderId 폴더 아이디
     */
    public void removeFolder(Long userId, Long folderId) {
        Folder folder = folderRepository.findByFolderIdAndUserId(folderId, userId);
        removeFolder(folder);
    }

    /**
     * 폴더를 삭제한다. 폴더 안에 데이터가 있으면 삭제하지 않는다.
     * @param folder 삭제할 폴더 데이터
     */
    private void removeFolder(Folder folder) {
        try {
            folderRepository.delete(folder);
        } catch (Exception e){
            throw new CustomException(ErrorCode.ASSOCIATED_DATA_EXISTS);
        }
    }

    public Long getWordCountInFolder(Long userId, Long folderId, Boolean memorization) {
        return folderQueryRepository.countWordInFolderById(userId, folderId, memorization);
    }
}
