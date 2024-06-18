package com.example.spring_word.test;

import jakarta.xml.bind.annotation.adapters.XmlAdapter;

public class BoldAdapter extends XmlAdapter<Bold, Boolean> {

    @Override
    public Bold marshal(final Boolean v) {
        return v != null && v ? new Bold() : null;
    }

    @Override
    public Boolean unmarshal(final Bold v) {
        return true;
    }
}
