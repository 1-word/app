package com.numo.api.global.comm.util;

import com.numo.api.global.comm.exception.CustomException;
import com.numo.api.global.comm.exception.ErrorCode;
import org.springframework.core.io.UrlResource;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;

public class FileUtil {

    public static UrlResource read(String path) {
        try {
            return new UrlResource("file:" + path);
        } catch (MalformedURLException e) {
            throw new CustomException(ErrorCode.FILE_READ_FAILED);
        }
    }

    public static boolean write(String path, MultipartFile multipartFile) {
        File file = new File(path);
        try {
            if (!file.exists()) {
                file.mkdirs();
            }
            multipartFile.transferTo(file);
        } catch (IOException e) {
            file.delete();
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public static boolean delete(String path) {
        return new File(path).delete();
    }

    public static String getFileExtension(String fileName) {
        String extension = StringUtils.getFilenameExtension(fileName);
        if (extension == null)
            return "";
        return extension;
    }
}
