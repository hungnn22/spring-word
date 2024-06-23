package com.example.spring_word;

import org.apache.poi.xwpf.usermodel.IBodyElement;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.springframework.util.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ParagraphReplacer implements WordReplacer {
    @Override
    public void replace(IBodyElement element, String regex, String newValue) {
        Pattern pattern = Pattern.compile(regex);
        if (!(element instanceof XWPFParagraph paragraph)) return;
        for (XWPFRun run : paragraph.getRuns()) {
            String text = run.text();
            if (!StringUtils.hasText(text)) return;
            Matcher matcher = pattern.matcher(text);
            if (!matcher.find()) return;
            String newText = matcher.replaceAll(newValue);
            run.setText(newText, 0);
        }
    }
}
