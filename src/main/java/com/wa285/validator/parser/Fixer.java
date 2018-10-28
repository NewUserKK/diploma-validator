package com.wa285.validator.parser;

import com.wa285.validator.parser.errors.Error;
import com.wa285.validator.parser.errors.Location;
import com.wa285.validator.parser.errors.critical.*;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFRun;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Fixer {
    private final List<Error> errors;
    private final XWPFDocument document;


    public Fixer(File file, ArrayList<Error> errors) throws IOException {
        this(new FileInputStream(file), errors);
    }

    public Fixer(InputStream is, ArrayList<Error> errors) throws IOException {
        this.errors = errors;
        try {
            this.document = new XWPFDocument(is);
        } catch (IOException e) {
            System.err.println("Error while creating docx document from input stream: " +
                    e.getMessage());
            throw e;
        }

        fixAll();
    }

    private void fixAll() {
        List<List<XWPFRun>> runs = new ArrayList<>();   //List<Paragraph>
        for (var paragraph : document.getParagraphs()) {
            runs.add(paragraph.getRuns());
        }

        for (Error error : errors) {
            if (error instanceof Critical) {
                Location location = error.getLocation();
                ((Critical) error).fix(runs.get(location.getParagraphNumber()).get(location.getRunNumber()));
            }
        }
    }
}
