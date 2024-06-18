package com.example.spring_word.test;

import java.util.ArrayList;
import java.util.List;

public class Helper {

    private Helper() {
    }

    /**
     * Returns the actual text of a Docx document.
     *
     * @param djmDocument the Document to process
     * @return String of actual text of the document
     */
    public static String getTextFromDocument(DJMDocument djmDocument) {
        StringBuilder stringBuilder = new StringBuilder();
        // Different elements can be of type BodyElement
        for (BodyElement bodyElement : djmDocument.getBody().getBodyElements()) {
            // Check, if current BodyElement is of type DJMParagraph
            if (bodyElement instanceof DJMParagraph dJMParagraph) {
                // Different elements can be of type ParagraphElement
                for (ParagraphElement paragraphElement : dJMParagraph.getParagraphElements()) {
                    // Check, if current ParagraphElement is of type DJMRun
                    if (paragraphElement instanceof DJMRun dJMRun) {
                        stringBuilder.append(dJMRun.getText());
                    }
                }
            }
        }
        return stringBuilder.toString();
    }

    /**
     * Returns a list of all bold words in a document.
     *
     * @param djmDocument the Document to process
     * @return list of all bold words
     */
    public static List<String> getBoldWords(DJMDocument djmDocument) {
        List<String> boldWords = new ArrayList<>();
        // Different elements can be of type BodyElement
        for (BodyElement bodyElement : djmDocument.getBody().getBodyElements()) {
            // Check, if current BodyElement is of type DJMParagraph
            if (bodyElement instanceof DJMParagraph dJMParagraph) {
                // Different elements can be of type ParagraphElement
                for (ParagraphElement paragraphElement : dJMParagraph.getParagraphElements()) {
                    // Check, if current ParagraphElement is of type DJMRun
                    if (paragraphElement instanceof DJMRun dJMRun) {
                        boolean isBold = dJMRun.getRunProperties().isBold();
                        if (isBold) {
                            String text = dJMRun.getText();
                            boldWords.add(text);
                        }
                    }
                }
            }
        }
        return boldWords;
    }
}
