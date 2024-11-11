package com.numo.wordapp.service.word.impl;

import com.numo.wordapp.comm.exception.CustomException;
import com.numo.wordapp.comm.exception.ErrorCode;
import com.numo.wordapp.dto.word.FolderDto;
import com.numo.wordapp.entity.word.Folder;
import com.numo.wordapp.repository.FolderRepository;
import com.numo.wordapp.service.word.FolderService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class FolderServiceImpl implements FolderService {
    private final FolderRepository folderRepository;

    public FolderServiceImpl(FolderRepository folderRepository){
        this.folderRepository = folderRepository;
    }


    @Override
    public List<Folder> getByFolderName(String user_id){
        return folderRepository.findByUserId(user_id);
    }

    @Override
    public Folder updateByFolder(FolderDto.Request fdto){
        Folder folder = folderRepository.findByFolderIdAndUserId(fdto.getFolder_id(), fdto.getUser_id())
                .orElseThrow(() -> new CustomException(ErrorCode.DATA_NOT_FOUND));
        //folder.setUserId(fdto.getUser_id());
        folder.setFolderName(fdto.getFolder_name());
        folder.setMemo(fdto.getMemo());
        folder.setColor(fdto.getColor());
        folder.setBackground(fdto.getBackground());
        return folderRepository.save(folder);
    }

    @Override
    public Folder setByFolder(FolderDto.Request fdto) {
        return folderRepository.save(fdto.toEntity());
    }

    @Transactional
    @Override
    public int removeByFolder(FolderDto.Request fdto){
        Folder folder = folderRepository.findByFolderIdAndUserId(fdto.getFolder_id(), fdto.getUser_id())
                .orElseThrow(() -> new CustomException(ErrorCode.DATA_NOT_FOUND));
        try {
            folderRepository.delete(folder);
        }catch (Exception e){
            throw new CustomException(ErrorCode.ASSOCIATED_DATA_EXISTS);
        }
        return fdto.getFolder_id();
    }
}
