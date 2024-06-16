package com.example.spring_word;

public interface FileHandlerFactory {
    FileHandler getFileHandler(FileType fileType);
}
