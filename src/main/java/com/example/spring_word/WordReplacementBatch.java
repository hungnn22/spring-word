package com.example.spring_word;

import java.util.ArrayList;
import java.util.List;

public class WordReplacementBatch {
    private List<WordReplacer> wordReplacerList;
    private String regex;
    private String newValue;

    public WordReplacementBatch regex(String regex) {
        this.regex = regex;
        return this;
    }

    public WordReplacementBatch add(WordReplacer wordReplacer) {
        wordReplacerList.add(wordReplacer);
        return this;
    }

//    public void process() {
//        wordReplacerList.stream().map(wordReplacer -> wordReplacer.)
//    }
}
