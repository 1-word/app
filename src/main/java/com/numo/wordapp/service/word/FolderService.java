//package com.numo.wordapp.service.word;
//
//import com.numo.wordapp.comm.exception.CustomException;
//import com.numo.wordapp.comm.exception.ErrorCode;
//import com.numo.wordapp.entity.word.Folder;
//import com.numo.wordapp.repository.FolderRepository;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.util.List;
//
//@Service
//@RequiredArgsConstructor
//public class FolderService {
//
//    private final FolderRepository folderRepository;
//
//    public List<Folder> getByFolderName(String user_id){
//        return folderRepository.findByUserId(user_id);
//    }
//
//    public Folder updateByFolder(FolderDto.Request fdto){
//        Folder folder = folderRepository.findByFolderIdAndUserId(fdto.getFolder_id(), fdto.getUser_id())
//                .orElseThrow(() -> new CustomException(ErrorCode.DATA_NOT_FOUND));
//        //folder.setUserId(fdto.getUser_id());
//        folder.setFolderName(fdto.getFolder_name());
//        folder.setMemo(fdto.getMemo());
//        folder.setColor(fdto.getColor());
//        folder.setBackground(fdto.getBackground());
//        return folderRepository.save(folder);
//    }
//
//    public Folder setByFolder(FolderDto.Request fdto) {
//        return folderRepository.save(fdto.toEntity());
//    }
//
//    @Transactional
//    public int removeByFolder(FolderDto.Request fdto){
//        Folder folder = folderRepository.findByFolderIdAndUserId(fdto.getFolder_id(), fdto.getUser_id())
//                .orElseThrow(() -> new CustomException(ErrorCode.DATA_NOT_FOUND));
//        try {
//            folderRepository.delete(folder);
//        }catch (Exception e){
//            throw new CustomException(ErrorCode.ASSOCIATED_DATA_EXISTS);
//        }
//        return fdto.getFolder_id();
//    }
//}
