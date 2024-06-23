package com.example.spring_word;

import org.apache.poi.xwpf.usermodel.IBodyElement;
import org.apache.poi.xwpf.usermodel.IRunBody;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.xmlbeans.XmlCursor;
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlObject;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTR;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DrawReplacer implements WordReplacer {

    private static final String DRAW_NAME_SPACE = "declare namespace w='http://schemas.openxmlformats.org/wordprocessingml/2006/main' .//*/w:txbxContent/w:p/w:r";

    @Override
    public void replace(IBodyElement element, String regex, String newValue) throws XmlException {
        if (!(element instanceof XWPFParagraph paragraph)) return;
        Pattern pattern = Pattern.compile(regex);
        try (XmlCursor cursor = paragraph.getCTP().newCursor();) {
            cursor.selectPath(DRAW_NAME_SPACE);
            List<XmlObject> xmlObjects = new ArrayList<>();
            while (cursor.hasNextSelection()) {
                cursor.toNextSelection();
                XmlObject obj = cursor.getObject();
                xmlObjects.add(obj);
            }
            for (XmlObject xmlObject : xmlObjects) {
                CTR ctr = CTR.Factory.parse(xmlObject.xmlText());
                XWPFRun bufferRun = new XWPFRun(ctr, (IRunBody) paragraph);
                String text = bufferRun.getText(0);
                if (StringUtils.hasText(text)) {
                    Matcher matcher = pattern.matcher(text);
                    if (matcher.find()) {
                        String newText = matcher.replaceAll(newValue);
                        bufferRun.setText(newText, 0);
                        xmlObject.set(bufferRun.getCTR());
                    }
                }
            }
        }
    }
}
