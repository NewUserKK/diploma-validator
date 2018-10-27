package com.wa285.validator.parser;

import com.wa285.validator.parser.errors.Error;
import org.apache.poi.xwpf.usermodel.XWPFDocument;

import java.io.*;
import java.util.ArrayList;

public class Parser {
    private ArrayList<Error> errors = new ArrayList<>();
    private final XWPFDocument document;

    public Parser(File file) throws IOException {
        this(new FileInputStream(file));
    }

    public Parser(InputStream is) throws IOException {
        try {
            this.document = new XWPFDocument(is);
        } catch (IOException e) {
            System.err.println("Error while creating docx document from input stream: " +
                    e.getMessage());
            throw e;
        }

        parse();
    }

    private void parse() {

    }

}
