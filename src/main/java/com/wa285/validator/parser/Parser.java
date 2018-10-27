package com.wa285.validator.parser;

import com.wa285.validator.parser.errors.Error;
import com.wa285.validator.parser.errors.Location;
import com.wa285.validator.parser.errors.critical.FieldSizeCriticalError;
import com.wa285.validator.parser.errors.critical.DocumentFormatCriticalError;
import com.wa285.validator.parser.errors.warning.MissingStructuralElementError;
import org.apache.poi.xwpf.usermodel.ParagraphAlignment;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTBody;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STPageOrientation;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.wa285.validator.parser.ElementSize.*;

// TODO: exceptions
public class Parser {
    private final List<Error> errors = new ArrayList<>();
    private final XWPFDocument document;
    private boolean parsed;

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

    public Parser(XWPFDocument document) {
        this.document = document;
    }

    public List<Error> findErrors() {
        if (!parsed) {
            parse();
        }
        return errors;
    }

    private void parse() {
        checkFormat();
        checkNumeration();
        this.parsed = true;
    }

    private void checkFormat() {
        var margin = document.getDocument().getBody().getSectPr().getPgMar();
        var leftMargin = margin.getLeft().intValue();
        var rightMargin = margin.getRight().intValue();
        var topMargin = margin.getTop().intValue();
        var bottomMargin = margin.getBottom().intValue();

//        if (leftMargin != LEFT_MARGIN.value()) {
        if (!LEFT_MARGIN.value().contains(leftMargin)) {
            errors.add(new FieldSizeCriticalError("LeftFieldSizeCriticalError", null));
        }

//        if (rightMargin != RIGHT_MARGIN.value()) {
        if (!RIGHT_MARGIN.value().contains(rightMargin)) {
            errors.add(new FieldSizeCriticalError("RightFieldSizeCriticalError", null));
        }

//        if (topMargin != TOP_MARGIN.value()) {
        if (!TOP_MARGIN.value().contains(topMargin)) {
            errors.add(new FieldSizeCriticalError("TopFieldSizeCriticalError", null));
        }

//        if (bottomMargin != BOTTOM_MARGIN.value()) {
        if (!BOTTOM_MARGIN.value().contains(bottomMargin)) {
            errors.add(new FieldSizeCriticalError("BottomFieldSizeCriticalError", null));
        }

        var pageSize = document.getDocument().getBody().getSectPr().getPgSz();
        var documentOrientation = pageSize.getOrient();
        var documentWidth = pageSize.getW().intValue();
        var documentHeight = pageSize.getH().intValue();

        if (documentOrientation != STPageOrientation.Enum.forInt(2)) {
            errors.add(new DocumentFormatCriticalError("A4", null));
        }

//        if (documentWidth != DOCUMENT_WIDTH.value()) {
        if (!DOCUMENT_WIDTH.value().contains(documentWidth)) {
            errors.add(new DocumentFormatCriticalError("WidthDocumentFormatCriticalError", null));
        }

//        if (documentHeight != DOCUMENT_HEIGHT.value()) {
        if (!DOCUMENT_HEIGHT.value().contains(documentHeight)) {
            errors.add(new DocumentFormatCriticalError("HeightDocumentFormatCriticalError", null));
        }

    }

    private void checkNumeration() {
        checkStructuralElements();
    }

    Map<StructuralElement, Boolean> structuralElementsCheck = new HashMap<>() {{
        for (var item: StructuralElement.values()) {
            put(item, false);
        }
    }};

    private void checkStructuralElements() {
        var paragraphs = document.getParagraphs();
        for (var paragraph: paragraphs) {
            var structuralElement = getStructuralElement(paragraph.getText());
            if (structuralElement != null) {
                structuralElementsCheck.put(structuralElement, true);
                if (paragraph.getAlignment() != ParagraphAlignment.CENTER) {
//                    errors.add()
                }
            }
        }
        for (var item: structuralElementsCheck.keySet()) {
            errors.add(new MissingStructuralElementError(item, new Location()));
        }
    }

    private StructuralElement getStructuralElement(String text) {
        for (var elem: StructuralElement.values()) {
            if (text.equals(elem.getTitle())) {
                return elem;
            }
        }
        return null;
    }

}
