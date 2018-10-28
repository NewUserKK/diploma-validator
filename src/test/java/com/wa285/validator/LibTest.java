package com.wa285.validator;

import com.wa285.validator.parser.ElementSize;
import com.wa285.validator.parser.Parser;
import org.apache.poi.xwpf.usermodel.BodyElementType;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.xmlbeans.XmlException;

import java.io.*;
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

    public static void main(String[] args) throws IOException, XmlException {
        var test = new LibTest();
        var doc = test.getDocument("Test.docx");
        var parser = new Parser(doc);
        System.out.println(parser.findErrors().stream().map(Object::toString)
                .collect(Collectors.joining("\n")));
    }
}