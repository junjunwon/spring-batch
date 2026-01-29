package com.springbatch.common.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Slf4j
public class MultipartFileUtils {

    // Path를 MultipartFile로 변환
    public static MultipartFile fromPath(Path filePath) throws IOException {
        String name = filePath.getFileName().toString();
        byte[] content = Files.readAllBytes(filePath);
        return new MockMultipartFile("file", name, Files.probeContentType(filePath), content);
    }

    // byte[]를 MultipartFile로 변환
    public static MultipartFile fromBytes(byte[] content, String originalFileName, String contentType) {
        return new MockMultipartFile("file", originalFileName, contentType, content);
    }

    public static MultipartFile fromPath(String imagePath) {
        try {
            File file = Paths.get(imagePath).toFile();
            String contentType = Files.probeContentType(file.toPath()); // 자동 감지 (e.g., image/png)

            try (FileInputStream input = new FileInputStream(file)) {
                return new MockMultipartFile("file", file.getName(), contentType, input);
            }
        } catch (IOException e) {
            log.error("이미지 변환 중 오류 발생: {}", imagePath, e);
            throw new RuntimeException(e);
        }
    }
}
