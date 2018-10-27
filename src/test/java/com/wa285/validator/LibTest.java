package com.wa285.validator;

import org.apache.poi.xwpf.usermodel.BodyElementType;
import org.apache.poi.xwpf.usermodel.XWPFDocument;

import java.io.FileNotFoundException;
import java.io.IOException;

public class LibTest {

    private XWPFDocument getDocument() throws IOException {
        var fis = getClass().getResourceAsStream("History.docx");
        if (fis == null) {
            throw new FileNotFoundException("Resource not found");
        }
//        var fis = new FileInputStream("History.txt");
        return new XWPFDocument(fis);
    }

    public static void main(String[] args) throws IOException {
        var test = new LibTest();
        var doc = test.getDocument();
        var tokens = doc.getBodyElements();
        for (var token: tokens) {
            if (token.getElementType() == BodyElementType.PARAGRAPH) {

            }
        }
    }
}
