package com.numo.wordapp.service.file;

import com.numo.wordapp.comm.util.FileUtil;
import com.numo.wordapp.entity.file.File;
import com.numo.wordapp.entity.user.User;
import com.numo.wordapp.repository.FileRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class FileService {
    private final FileRepository fileRepository;
    private final String basicPath;

    public FileService(FileRepository fileRepository,
                       @Value("${cstm.file.path}") String basicPath) {

        this.fileRepository = fileRepository;
        if (!basicPath.endsWith("/")) {
            basicPath += "/";
        }
        this.basicPath = basicPath;
    }

    public List<String> upload(Long userId, ArrayList<MultipartFile> files) {
        List<String> uploadFiles = new ArrayList<>();
        for (MultipartFile file : files) {
            uploadFiles.add(upload(userId, file));
        }
        return uploadFiles;
    }

    public String upload(Long userId, MultipartFile file) {
        String oriName = file.getOriginalFilename();
        String extension = FileUtil.getFileExtension(oriName);
        String saveName = UUID.randomUUID().toString().substring(1, 8) + System.currentTimeMillis();
        String savePath = basicPath + saveName + "." + extension;
        if (FileUtil.write(savePath, file)) {
            File saveFile = File.builder()
                    .user(User.builder().userId(userId).build())
                    .path(savePath)
                    .oriName(oriName)
                    .extension(extension)
                    .build();
            return fileRepository.save(saveFile).getFileId();
        }
        return null;
    }

    public Resource download(Long userId, String fileId) {
        File file = getFileAndCheckPermission(userId, fileId);
        return FileUtil.read(file.getPath());
    }

    public Resource download(String fileId) {
        File file = findFileById(fileId);
        return FileUtil.read(file.getPath());
    }
    
    public void delete(Long userId, String fileId) {
        File file = getFileAndCheckPermission(userId, fileId);
        fileRepository.delete(file);
        FileUtil.delete(file.getPath());
    }

    private File getFileAndCheckPermission(Long userId, String fileId) {
        File file = findFileById(fileId);
        file.checkPermission(userId);
        return file;
    }

    private File findFileById(String fileId) {
        return fileRepository.findFileById(fileId);
    }
}
