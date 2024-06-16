package com.example.spring_word;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/file")
@RequiredArgsConstructor
public class FileController {
    private final FileHandlerFactory fileHandlerFactory;

    @GetMapping("/read")
    String read(@RequestPart MultipartFile file, FileType type) throws IOException {
        return fileHandlerFactory.getFileHandler(type).read(file);
    }
}
