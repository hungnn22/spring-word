package com.example.spring_word.test;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlElements;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

import java.util.List;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class DJMBody {

    List<BodyElement> bodyElements;

    @XmlElements({
            @XmlElement(name = "p", type = DJMParagraph.class)
    })

    public List<BodyElement> getBodyElements() {
        return bodyElements;
    }

    private void setBodyElements(List<BodyElement> bodyElements) {
        this.bodyElements = bodyElements;
    }
}
