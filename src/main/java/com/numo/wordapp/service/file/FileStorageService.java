package com.numo.wordapp.service.file;

import com.numo.wordapp.comm.util.FileUtil;
import com.numo.wordapp.conf.PropertyConfig;
import com.numo.wordapp.dto.file.FileDto;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@Service
public class FileStorageService {
    private final String basicPath;

    public FileStorageService(PropertyConfig propertyConfig) {
        this.basicPath =  propertyConfig.getPathWithEndFileSeparation();
    }

    public Resource read(String path) {
        return FileUtil.read(path);
    }

    public FileDto save(String middlePath, MultipartFile file) {
        FileDto result = null;
        middlePath = checkPath(middlePath);

        String oriName = file.getOriginalFilename();
        String extension = FileUtil.getFileExtension(oriName);
        String saveName = UUID.randomUUID().toString().substring(1, 8) + System.currentTimeMillis();
        String savePath = basicPath + middlePath + saveName + "." + extension;

        if (FileUtil.write(savePath, file)) {
            result = FileDto.builder()
                    .oriName(oriName)
                    .extension(extension)
                    .saveName(saveName)
                    .path(savePath)
                    .build();
        }

        return result;
    }

    public void delete(String path) {
        FileUtil.delete(path);
    }

    private String checkPath(String path) {
        if (!path.endsWith("/") && !path.isEmpty()) {
            path += "/";
        }
        return path;
    }
}
