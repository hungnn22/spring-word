package com.example.spring_word;

import lombok.extern.slf4j.Slf4j;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.*;
import org.apache.xmlbeans.XmlCursor;
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlObject;
import org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.CTInline;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDrawing;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTR;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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
            List<XWPFParagraph> paragraphs = document.getParagraphs();
            for (XWPFParagraph paragraph : paragraphs) {
                List<XWPFRun> runs = paragraph.getRuns();
                for (XWPFRun run : runs) {
                    // Check if the run contains text

                    final List<String> strings = extractValues(run.getCTR().xmlText());
                    if (!CollectionUtils.isEmpty(strings)) {
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

    private static void duplicatePage(XWPFDocument document) {
        // Lấy nội dung của trang cuối cùng
        XWPFParagraph[] paragraphs =
                document.getParagraphs()
                        .toArray(new XWPFParagraph[0]);

        // Tạo một trang mới bằng cách chèn ngắt trang
        document.createParagraph()
                .setPageBreak(true);

        // Tạo một bản sao của các đoạn văn trên trang cuối
        for (XWPFParagraph paragraph : paragraphs) {
            document.createParagraph()
                    .createRun()
                    .setText(paragraph.getText());
        }
    }

    public static void process(String filePath, Map<String, String> replacement, String outputPath) throws IOException {
        try (FileInputStream fis = new FileInputStream(filePath);
             XWPFDocument document = new XWPFDocument(fis)) {
            int numPages =
                    document.getProperties()
                            .getExtendedProperties()
                            .getUnderlyingProperties()
                            .getPages();

            // Nhân bản tất cả các trang
//            for (int i = 0; i < numPages; i++) {
//                duplicatePage(document);
//            }

            replaceTextInParagraphs(document, replacement);
            replaceTextInTables(document, replacement);
            replaceTextInShapes(document, replacement);

            try (FileOutputStream fos = new FileOutputStream(outputPath)) {
                document.write(fos);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static void replaceTextInParagraphs(XWPFDocument document, Map<String, String> replacement) {
        for (XWPFParagraph paragraph : document.getParagraphs()) {
            for (XWPFRun run : paragraph.getRuns()) {
                String text = run.getText(0);
                if (text != null) {
                    for (var entry : replacement.entrySet()) {
                        text = text.replace(entry.getKey(), entry.getValue());
                    }
                    run.setText(text, 0);
                }
            }
        }
    }


    private static void replaceTextInTables(XWPFDocument document, Map<String, String> replacement) {
        for (XWPFTable table : document.getTables()) {
            for (XWPFTableRow row : table.getRows()) {
                for (XWPFTableCell cell : row.getTableCells()) {
                    for (XWPFParagraph paragraph : cell.getParagraphs()) {
                        for (XWPFRun run : paragraph.getRuns()) {
                            String text = run.getText(0);
                            if (text != null) {
                                for (Map.Entry<String, String> entry : replacement.entrySet()) {
                                    text = text.replace(entry.getKey(), entry.getValue());
                                }
                                run.setText(text, 0);
                            }
                        }
                    }
                }
            }
        }
    }

    private static void replaceTextInShapes(XWPFDocument document, Map<String, String> replacement) throws XmlException {
        for (XWPFParagraph paragraph : document.getParagraphs()) {
            for (XWPFRun run : paragraph.getRuns()) {
                List<XmlObject> xmlObjects = new ArrayList<>();

                try (XmlCursor cursor = run.getCTR().newCursor()) {
                    cursor.selectPath(
                            "declare namespace w='http://schemas.openxmlformats.org/wordprocessingml/2006/main' .//*/w:txbxContent/w:p/w:r");

                    while (cursor.hasNextSelection()) {
                        cursor.toNextSelection();
                        XmlObject obj = cursor.getObject();
                        xmlObjects.add(obj);
                    }
                }

                for (XmlObject obj : xmlObjects) {
                    CTR ctr = CTR.Factory.parse(obj.xmlText());
                    XWPFRun bufferRun = new XWPFRun(ctr, (IRunBody) paragraph);
                    String text = bufferRun.getText(0);
                    if (text != null) {
                        for (Map.Entry<String, String> entry : replacement.entrySet()) {
                            text = text.replace(entry.getKey(), entry.getValue());
                        }
                        bufferRun.setText(text, 0);
                    }
                    obj.set(bufferRun.getCTR());
                }
            }
        }
    }
}
