package com.example.spring_word;

import org.apache.poi.xwpf.usermodel.IBodyElement;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;
import org.springframework.util.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TableReplacer implements WordReplacer {
    @Override
    public void replace(IBodyElement element, String regex, String newValue) {
        if (!(element instanceof XWPFTable table)) return;
        Pattern pattern = Pattern.compile(regex);
        for (XWPFTableRow row : table.getRows()) {
            for (XWPFTableCell cell : row.getTableCells()) {
                String text = cell.getText();
                if (!StringUtils.hasText(text)) return;
                Matcher matcher = pattern.matcher(text);
                if (!matcher.find()) return;
                String newText = matcher.replaceAll(newValue);
                cell.setText(newText);
            }
        }
    }
}
