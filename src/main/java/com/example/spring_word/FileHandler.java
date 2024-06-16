package com.example.spring_word;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface FileHandler {
    String read(MultipartFile multipartFile) throws IOException;
    byte[] write(MultipartFile multipartFile, String tag);
}
