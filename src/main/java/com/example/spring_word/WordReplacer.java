package com.example.spring_word;

import org.apache.poi.xwpf.usermodel.IBodyElement;
import org.apache.xmlbeans.XmlException;

public interface WordReplacer {
    void replace(IBodyElement element, String regex, String newValue) throws XmlException;
}
