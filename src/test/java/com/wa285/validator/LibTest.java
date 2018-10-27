package com.wa285.validator;

import com.wa285.validator.parser.ElementSize;
import com.wa285.validator.parser.Parser;
import org.apache.poi.xwpf.usermodel.BodyElementType;
import org.apache.poi.xwpf.usermodel.XWPFDocument;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.util.stream.Collectors;

public class LibTest {

    private XWPFDocument getDocument(String name) throws IOException {
        var fis = getClass().getResourceAsStream(name);
        if (fis == null) {
            throw new FileNotFoundException("Resource not found");
        }
//        var fis = new FileInputStream("History.txt");
        return new XWPFDocument(fis);
    }

    public static void main(String[] args) throws IOException {
        var test = new LibTest();
        var doc = test.getDocument("History.docx");
//        var tokens = doc.getBodyElements();
//        for (var token: tokens) {
//            if (token.getElementType() == BodyElementType.PARAGRAPH) {
//
//            }
////            for (var paragraph: token.getBody().getParagraphs()) {
////                System.out.println(paragraph.getStyle());
////            }
//        }
//        var margin = doc.getDocument().getBody().getSectPr().getPgMar();
//        System.out.println("Абзацный" + margin.getLeft());
//        System.out.println("Right" + margin.getRight());
//        System.out.println("Top/Bottom" + margin.getBottom());
//        doc.getDocument().getBody().getSectPr().getPgSz().getOrient();
//        doc.getDocument().getBody().getSectPr().getPgSz().getH();
//        System.out.println(doc.getDocument().getBody().getSectPr().getPgSz().getH().intValue());
//        var out = new FileOutputStream("Kslf.docx");
//        doc.write(out);
//        out.close();
//        for (int i = 0; i < 6; i++) {
//            System.out.println(doc.getDocument().getBody().getSectPr().getPgNumType().getStart().intValue());
//            System.out.println(doc.getDocument().getBody().getPArray(2).;
//
//        }
//        for (var par: doc.getParagraphs()) {
//            System.out.print(">>");
//            for (var run: par.getRuns()) {
//                System.out.print("[" + run.toString() + "]");
//            }
//            System.out.print(par.getText());
//            System.out.println("<<");
//        }
        doc.getProperties().getExtendedProperties().getPages();
        var parser = new Parser(doc);
        System.out.println(parser.findErrors().stream().map(Object::toString)
                .collect(Collectors.joining("\n")));
    }
}
