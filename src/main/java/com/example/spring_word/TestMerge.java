package com.example.spring_word;

import org.apache.poi.xwpf.usermodel.*;

import java.io.*;
import java.util.regex.Pattern;

public class TestMerge {
    public static final String REGEX = "\\{!!(.*?)}";

    static String basePath;
    static Pattern pattern;

    static {
        basePath = System.getProperty("user.dir") + "/src/main/resources/";
        pattern = Pattern.compile(REGEX);
    }

    public static void main(String[] args) {
        String filePath = basePath + "input.docx";
        try (FileInputStream fis = new FileInputStream(filePath);
             XWPFDocument sourceDocument = new XWPFDocument(fis);
             FileOutputStream fos = new FileOutputStream(basePath + "result.docx");) {
            String newValue = "NewValue";
            for (IBodyElement bodyElement : sourceDocument.getBodyElements()) {
                new ParagraphReplacer().replace(bodyElement, REGEX, newValue);
                new DrawReplacer().replace(bodyElement, REGEX, newValue);
                new TableReplacer().replace(bodyElement, REGEX, newValue);
            }
            sourceDocument.write(fos);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
