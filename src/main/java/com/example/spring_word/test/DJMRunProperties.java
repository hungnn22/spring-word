package com.example.spring_word.test;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
@XmlAccessorType(XmlAccessType.FIELD)
public class DJMRunProperties {

    @XmlElement
    DJMColor color;
    @XmlElement(name = "rFonts")
    DJMFont font;
    @XmlElement(name = "sz")
    FontSize fontSize;
    @XmlElement(name = "b")
    @XmlJavaTypeAdapter(BoldAdapter.class)
    @Getter(AccessLevel.NONE)
    Boolean isBold = false;

    public Boolean isBold() {
        return isBold;
    }
}
