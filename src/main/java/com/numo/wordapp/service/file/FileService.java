package com.numo.wordapp.service.file;

import com.numo.wordapp.conf.PropertyConfig;
import com.numo.wordapp.dto.file.FileDto;
import com.numo.wordapp.entity.file.File;
import com.numo.wordapp.entity.user.User;
import com.numo.wordapp.repository.FileRepository;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Service
public class FileService {
    private final FileRepository fileRepository;
    private final FileStorageService fileStorageService;

    public FileService(FileRepository fileRepository,
                       FileStorageService fileStorageService,
                       PropertyConfig propertyConfig) {
        this.fileRepository = fileRepository;
        this.fileStorageService = fileStorageService;
    }

    public List<String> uploadAndSave(Long userId, String middlePath, ArrayList<MultipartFile> files) {
        List<String> uploadFiles = new ArrayList<>();
        for (MultipartFile file : files) {
            uploadFiles.add(uploadAndSave(userId, middlePath, file));
        }
        return uploadFiles;
    }

    public String uploadAndSave(Long userId, String middlePath, MultipartFile file) {
        FileDto fileDto = storeFile(middlePath, file);
        File saveFile = File.builder()
                .user(User.builder().userId(userId).build())
                .path(fileDto.path())
                .oriName(fileDto.oriName())
                .extension(fileDto.extension())
                .build();
        return fileRepository.save(saveFile).getFileId();
    }

    public FileDto storeFile(String middlePath, MultipartFile file) {
        return fileStorageService.save(middlePath, file);
    }

    public Resource download(Long userId, String fileId) {
        File file = getFileAndCheckPermission(userId, fileId);
        return readFile(file.getPath());
    }

    public Resource download(String fileId) {
        File file = findFileById(fileId);
        return readFile(file.getPath());
    }

    public void delete(Long userId, String fileId) {
        File file = getFileAndCheckPermission(userId, fileId);
        fileRepository.delete(file);
        deleteFile(file.getPath());
    }

    private void deleteFile(String path) {
        fileStorageService.delete(path);
    }

    private Resource readFile(String path) {
        return fileStorageService.read(path);
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
