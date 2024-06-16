package com.example.spring_word;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DefaultFileHandlerFactory implements FileHandlerFactory{
    private final WordFileHandler wordFileHandler;

    @Override
    public FileHandler getFileHandler(FileType fileType) {
        return switch (fileType) {
            case WORD -> wordFileHandler;
        };
    }
}
