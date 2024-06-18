package com.example.spring_word.test;

import com.example.spring_word.FileHandler;
import com.example.spring_word.WordFileHandler;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class Test {

    public static final String BASE = "/home/hungnn1@kaopiz.local/Documents/Code/spring-word/src/main/resources/";

    public static void main(String[] args) throws IOException {
        String input = "input.docx";
        String output = "output.docx";
        final Map<String, String> replacement = new HashMap<>();
        replacement.put("{!!改ページ}", "改ページ");
        replacement.put("{!!ZIPCODE}", "ZIPCODE");
        replacement.put("{!!署名欄２}", "署名欄２");
        replacement.put("{!!領収金額}", "領収金額");
        replacement.put("{!!当日日付}", "当日日付");
        replacement.put("{!!領収書番号}", "領収書番号");
        replacement.put("{!!領収書番号}", "領収書番号");
        replacement.put("{!!領収書署名欄１}", "領収書署名欄１");
        try {
            WordFileHandler.process(BASE + input, replacement, BASE +  output);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
