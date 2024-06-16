package com.example.spring_word;

import lombok.extern.slf4j.Slf4j;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.*;
import org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.CTInline;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDrawing;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@Slf4j
public class WordFileHandler implements FileHandler {

    @Override
    public String read(MultipartFile multipartFile) {
        var extractedText = new StringBuilder();
        try (var inputStream = multipartFile.getInputStream();
             var document = new XWPFDocument(inputStream)) {
            POIFSFileSystem poifsFileSystem = new POIFSFileSystem(inputStream);
            List<XWPFParagraph> paragraphs = document.getParagraphs();
            for (XWPFParagraph paragraph : paragraphs) {
                List<XWPFRun> runs = paragraph.getRuns();
                for (XWPFRun run : runs) {
                    // Check if the run contains text

                    final List<String> strings = extractValues(run.getCTR().xmlText());
                    if (!CollectionUtils.isEmpty(strings)){
                        extractedText.append(strings).append("\n");
                    }
                }
            }

        } catch (Exception e) {
            log.error("read file error: ", e);
        }

        return extractedText.toString();
    }

    public static List<String> extractValues(String input) {
        String patternString = "\\{!![^}]+}";
        Pattern pattern = Pattern.compile(patternString);
        Matcher matcher = pattern.matcher(input);

        List<String> matches = new ArrayList<>();

        while (matcher.find()) {
            matches.add(matcher.group());
        }

        return matches;
    }

    @Override
    public byte[] write(MultipartFile multipartFile, String tag) {
        return new byte[0];
    }
}
