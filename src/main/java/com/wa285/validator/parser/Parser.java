package com.wa285.validator.parser;

import com.wa285.validator.parser.errors.Error;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTBody;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

// TODO: exceptions
public class Parser {
    private final List<Error> errors = new ArrayList<>();
    private final XWPFDocument document;
    private final CTBody body;

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
        body = document.getDocument().getBody();

        parse();
    }

    private void parse() {

    }

    private void parseNumeration() {

    }

    private void parseStructural() {
    }

}
