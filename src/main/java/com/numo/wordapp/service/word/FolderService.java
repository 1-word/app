package com.numo.wordapp.service.word;

import com.numo.wordapp.comm.exception.CustomException;
import com.numo.wordapp.comm.exception.ErrorCode;
import com.numo.wordapp.dto.folder.FolderRequestDto;
import com.numo.wordapp.dto.folder.FolderResponseDto;
import com.numo.wordapp.dto.folder.FolderUpdateDto;
import com.numo.wordapp.entity.word.Folder;
import com.numo.wordapp.repository.folder.FolderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FolderService {

    private final FolderRepository folderRepository;

    /**
     * 폴더 다건 조회
     * @param userId 유저 아이디
     * @return 조회한 폴더 데이터
     */
    public List<FolderResponseDto> getFolders(Long userId){
       return getFolders(userId, null);
    }

    /**
     * 폴더 다건 조회
     * @param userId 유저 아이디(Nullable)
     * @param folderId 폴더 아이디(Nullable)
     * @return 조회한 폴더 데이터
     */
    public List<FolderResponseDto> getFolders(Long userId, Long folderId){
        List<Folder> folders = folderRepository.findFoldersByUserId(userId, folderId);
        List<FolderResponseDto> res = folders.stream().map(FolderResponseDto::of).toList();
        return res;
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
     * 폴더 삭제
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
}
