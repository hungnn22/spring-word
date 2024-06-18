package com.example.spring_word.test;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlElements;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

import java.util.List;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class DJMParagraph implements BodyElement {

    List<ParagraphElement> paragraphElements;

    @XmlElements({
            @XmlElement(name = "r", type = DJMRun.class),
    })

    public List<ParagraphElement> getParagraphElements() {
        return paragraphElements;
    }

    private void setParagraphElements(List<ParagraphElement> paragraphElements) {
        this.paragraphElements = paragraphElements;
    }

}
