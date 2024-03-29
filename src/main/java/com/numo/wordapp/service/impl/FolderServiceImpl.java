package com.numo.wordapp.service.impl;

import com.numo.wordapp.comm.advice.exception.CustomException;
import com.numo.wordapp.comm.advice.exception.ErrorCode;
import com.numo.wordapp.dto.FolderDto;
import com.numo.wordapp.model.word.Folder;
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
                .orElseThrow(() -> new CustomException(ErrorCode.DataNotFound));
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

    @Override
    public String removeByFolder(FolderDto.Request fdto){
        Folder folder = folderRepository.findByFolderIdAndUserId(fdto.getFolder_id(), fdto.getUser_id())
                .orElseThrow(() -> new CustomException(ErrorCode.DataNotFound));
        try {
            folderRepository.delete(folder);
        }catch (Exception e){
            throw new CustomException(ErrorCode.AssociatedDataExists);
        }
        return "데이터 삭제를 완료하였습니다.";
    }
}
