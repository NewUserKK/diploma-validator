package com.wa285.validator.parser;

import com.wa285.validator.parser.errors.Error;
import com.wa285.validator.parser.errors.critical.FieldSizeCriticalError;
import com.wa285.validator.parser.errors.critical.DocumentFormatCriticalError;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTBody;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STPageOrientation;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import static com.wa285.validator.parser.ElementSize.*;

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
        checkFormat();
    }

    private void checkFormat() {
        var margin = document.getDocument().getBody().getSectPr().getPgMar();
        var leftMargin = margin.getLeft().intValue();
        var rightMargin = margin.getRight().intValue();
        var topMargin = margin.getTop().intValue();
        var bottomMargin = margin.getBottom().intValue();

        if (leftMargin != LEFT_MARGIN.value()) {
            errors.add(new FieldSizeCriticalError("LeftFieldSizeCriticalError", null));
        }

        if (rightMargin != RIGHT_MARGIN.value()) {
            errors.add(new FieldSizeCriticalError("RightFieldSizeCriticalError", null));
        }

        if (topMargin != TOP_MARGIN.value()) {
            errors.add(new FieldSizeCriticalError("TopFieldSizeCriticalError", null));
        }

        if (bottomMargin != BOTTOM_MARGIN.value()) {
            errors.add(new FieldSizeCriticalError("BottomFieldSizeCriticalError", null));
        }

        var pageSize = document.getDocument().getBody().getSectPr().getPgSz();
        var documentOrientation = pageSize.getOrient();
        var documentWidth = pageSize.getW().intValue();
        var documentHeight = pageSize.getH().intValue();

        if (documentOrientation != STPageOrientation.Enum.forInt(2)) {
            errors.add(new DocumentFormatCriticalError("A4", null));
        }

        if (documentWidth != DOCUMENT_WIDTH.value()) {
            errors.add(new DocumentFormatCriticalError("WidthDocumentFormatCriticalError", null));
        }

        if (documentHeight != DOCUMENT_HEIGHT.value()) {
            errors.add(new DocumentFormatCriticalError("HeightDocumentFormatCriticalError", null));
        }




    }

    private void parseNumeration() {

    }

    private void parseStructural() {
    }

}
