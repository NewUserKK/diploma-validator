package com.wa285.validator;

import com.wa285.validator.parser.ElementSize;
import org.apache.poi.xwpf.usermodel.BodyElementType;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.xmlbeans.XmlException;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTColor;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTHpsMeasure;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.impl.CTHpsMeasureImpl;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigInteger;

public class LibTest {

    private XWPFDocument getDocument() throws IOException {
        var fis = getClass().getResourceAsStream("History.docx");
        if (fis == null) {
            throw new FileNotFoundException("Resource not found");
        }
//        var fis = new FileInputStream("History.txt");
        return new XWPFDocument(fis);
    }

    public static void main(String[] args) throws IOException, XmlException {
        var test = new LibTest();
        var doc = test.getDocument();
        var tokens = doc.getBodyElements();
//        for (var token: tokens) {
//            if (token.getElementType() == BodyElementType.PARAGRAPH) {
//
//            }
////            for (var paragraph: token.getBody().getParagraphs()) {
////                System.out.println(paragraph.getStyle());
////            }
//        }
        var margin = doc.getDocument().getBody().getSectPr().getPgMar();
        System.out.println("Абзацный" + margin.getLeft());
        System.out.println("Right" + margin.getRight());
        System.out.println("Top/Bottom" + margin.getBottom());
        doc.getDocument().getBody().getSectPr().getPgSz().getOrient();
        doc.getDocument().getBody().getSectPr().getPgSz().getH();
        System.out.println(doc.getDocument().getBody().getSectPr().getPgSz().getH().intValue());

        for (var paragraph : doc.getParagraphs()) {
            for (var run : paragraph.getRuns()) {
                System.out.println(run.getFontName());
            }
        }

        System.out.println(doc.getStyle().getDocDefaults());

        var out = new FileOutputStream("Kslf.docx");
        doc.write(out);
        out.close();


    }
}
