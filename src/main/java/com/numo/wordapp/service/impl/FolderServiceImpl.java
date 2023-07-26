package com.numo.wordapp.service.impl;

import com.numo.wordapp.comm.advice.exception.CustomException;
import com.numo.wordapp.comm.advice.exception.ErrorCode;
import com.numo.wordapp.dto.FolderDto;
import com.numo.wordapp.model.Folder;
import com.numo.wordapp.repository.FolderRepository;
import com.numo.wordapp.service.FolderService;
import org.springframework.stereotype.Service;

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
                .orElseThrow(() -> new CustomException(ErrorCode.DataNotFound.getDescription()));
        //folder.setUserId(fdto.getUser_id());
        folder.setFolderName(fdto.getFolder_name());
        folder.setMemo(fdto.getMemo());
        return folderRepository.save(folder);
    }

    @Override
    public Folder setByFolder(FolderDto.Request fdto) {
        return folderRepository.save(fdto.toEntity());
    }

    @Override
    public String removeByFolder(FolderDto.Request fdto){
        Folder folder = folderRepository.findByFolderIdAndUserId(fdto.getFolder_id(), fdto.getUser_id())
                .orElseThrow(() -> new CustomException(ErrorCode.DataNotFound.getDescription()));
        folderRepository.delete(folder);
        return "데이터 삭제를 완료하였습니다.";
    }
}
