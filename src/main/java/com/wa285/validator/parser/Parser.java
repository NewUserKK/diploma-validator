package com.wa285.validator.parser;

import com.wa285.validator.parser.errors.Error;
import com.wa285.validator.parser.errors.Location;
import com.wa285.validator.parser.errors.critical.*;
import com.wa285.validator.parser.errors.critical.enumeration.EnumerationNumberingError;
import com.wa285.validator.parser.errors.critical.structural.StructuralElementCenteringError;
import com.wa285.validator.parser.errors.critical.structural.StructuralElementMissingBoldError;
import com.wa285.validator.parser.errors.warning.MissingStructuralElementError;
import org.apache.poi.xwpf.usermodel.ParagraphAlignment;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.xmlbeans.XmlException;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRPr;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import static com.wa285.validator.parser.ElementSize.*;

// TODO: exceptions
public class Parser {
    private List<Error> errors;
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
    }

    public Parser(XWPFDocument document) {
        this.document = document;
    }

    public List<Error> findErrors() {
        if (errors == null) {
            parse();
        }
        return errors;
    }

    private void parse() {
        errors = new ArrayList<>();
        checkFormat();
        checkNumeration();
    }

    private void checkFormat() {
        CTRPr defaultValues = null;
        try {
            defaultValues = document.getStyle().getDocDefaults().getRPrDefault().getRPr();
        } catch (XmlException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        var defaultSize = defaultValues.getSz().getVal().intValue() / 2;
        var defaultFont = defaultValues.getRFonts().getAscii();

        var margin = document.getDocument().getBody().getSectPr().getPgMar();
        var leftMargin = margin.getLeft().intValue();
        var rightMargin = margin.getRight().intValue();
        var topMargin = margin.getTop().intValue();
        var bottomMargin = margin.getBottom().intValue();

        if (!LEFT_MARGIN.value().contains(leftMargin)) {
            errors.add(new FieldSizeError("Левое поле должно быть равно 30 мм: " + leftMargin * 0.017638889 + " мм здесь", null));
        }

        if (!RIGHT_MARGIN.value().contains(rightMargin)) {
            errors.add(new FieldSizeError("Правое поле должно быть равно 15 мм: " + rightMargin * 0.017638889 + " мм здесь", null));
        }

        if (!TOP_MARGIN.value().contains(topMargin)) {
            errors.add(new FieldSizeError("Верхнее поле должно быть равно 20 мм: " + topMargin * 0.017638889 + " мм здесь", null));
        }

        if (!BOTTOM_MARGIN.value().contains(bottomMargin)) {
            errors.add(new FieldSizeError("Нижнее поле должно быть равно 20 мм: " + bottomMargin * 0.017638889 + " мм здесь", null));
        }

        var pageSize = document.getDocument().getBody().getSectPr().getPgSz();
        var documentWidth = pageSize.getW().intValue();
        var documentHeight = pageSize.getH().intValue();

        if (!DOCUMENT_WIDTH.value().contains(documentWidth)) {
            errors.add(new DocumentFormatError("Формат страниц должен быть вертикальный A4", null));
        }

        if (!DOCUMENT_HEIGHT.value().contains(documentHeight)) {
            errors.add(new DocumentFormatError("Формат страниц должен быть вертикальный A4", null));
        }

        var paragraphs = document.getParagraphs();
        for (int i = 0; i < paragraphs.size(); i++) {
            var textStart = 0;

            var runs = paragraphs.get(i).getRuns();
            for (int j = 0; j < runs.size(); j++) {
                var run = runs.get(j);
                var textEnd = textStart + run.toString().length();
                Location location = new Location(i, textStart, textEnd, j);

                if (run.getColor() != null) {
                    errors.add(new FontColorError("Шрифт должен быть чёрным: #" + run.getColor() + " здесь", null));
                }

                if ((run.getFontName() == null && !defaultFont.equals("Times New Roman")) || !run.getFontFamily().equals("Times New Roman")) {
                    errors.add(new FontStyleError("Шрифт должен быть Times New Roman: " + run.getFontFamily() + " здесь", null));
                }

                if ((run.getFontSize() == -1 && defaultSize < 12) || run.getFontSize() < 12) {
                    errors.add(new FontSizeError("Размер шрифта должен быть не меньше 12 пт: " + run.getFontSize() + " пт здесь", location));
                }

                textStart = textEnd;
            }
        }
    }

    private void checkNumeration() {
        checkStructuralElements();
    }

    /*
     * TODO: add dot check in the end
     * TODO: add lower/upper check
     */
    private void checkStructuralElements() {
        Map<StructuralElement, Boolean> structuralElementsCheck = new HashMap<>() {{
            for (var item : StructuralElement.values()) {
                put(item, false);
            }
        }};

        var paragraphs = document.getParagraphs();
        for (int i = 0; i < paragraphs.size(); i++) {
            var paragraph = paragraphs.get(i);
            var structuralElement = getStructuralElement(paragraph.getText());

            if (structuralElement != null) {
                structuralElementsCheck.put(structuralElement, true);
                if (paragraph.getAlignment() != ParagraphAlignment.CENTER) {
                    errors.add(new StructuralElementCenteringError(
                            structuralElement, "Должно быть по центру",
                            new Location(i, 0, paragraph.getText().length())
                    ));
                }

                var textStart = 0;
                var runs = paragraph.getRuns();
                for (int j = 0; j < runs.size(); j++) {
                    var run = runs.get(j);
                    var textEnd = textStart + run.text().length();
                    if (!run.isBold()) {
                        errors.add(new StructuralElementMissingBoldError(
                                structuralElement, "Шрифт должен быть жирным",
                                new Location(i, textStart, textEnd, j)
                        ));
                    }
                    textStart = textEnd;
                }
            }
        }

        for (var item : structuralElementsCheck.keySet()) {
            errors.add(new MissingStructuralElementError(item, null));
        }
    }

    private StructuralElement getStructuralElement(String text) {
        for (var elem : StructuralElement.values()) {
            if (text.equals(elem.getTitle())) {
                return elem;
            }
        }
        return null;
    }

    private void parseEnumerations() {
        // TODO: nested enumerations
        // TODO: complex numbering (1.1. ...)
        // TODO: numbering from zero

        final var NONE = 0;
        final var DASH = 1;
        final var DIGIT = 2;
        final var LETTER = 3;

        final var dashPattern = Pattern.compile("^\\p{Space}*- .*$");  // matches -
        final var digitPattern = Pattern.compile("^\\p{Space}*[1-9]\\d*\\) .*$");  // matches xy...)
        final var letterPattern = Pattern.compile("^\\p{Space}*([а-я]) .*$");  // matches x)

        final var forbiddenLetters = new ArrayList<>() {{
            add('ё');
            add('з');
            add('й');
            add('о');
            add('ч');
            add('ъ');
            add('ы');
            add('ь');
        }};

        var paragraphs = document.getParagraphs();

        // TODO: - and big '-'
        var prevLine = "";
        var prevType = NONE;
        var prevStart = "";
        for (int i = 0; i < paragraphs.size(); i++) {
            var paragraph = paragraphs.get(i);
            var line = paragraph.getText();

            if (line.startsWith("-") && prevLine.startsWith("-")) {
                prevType = DASH;
                // TODO: check ,/;
            } else if (digitPattern.matcher(line).matches()) {  // it's a digit enum
                var split = line.split("\\)");
                var currentDigit = split[0];
                // TODO: check better, through regex?
                if (prevType == NONE) {
                    prevType = DIGIT;
                    prevStart = currentDigit;

                } else if (prevType == DIGIT) {
                    if (Integer.parseInt(currentDigit) - 1 != Integer.parseInt(prevStart)) {
                        errors.add(new EnumerationNumberingError(
                                "Нумрация должжна быть последовательной",
                                new Location(i, 0, line.length())
                        ));
                    }
                    prevStart = currentDigit;

                } else if (prevType == DASH) {
                    // TODO: complex lists
                } else if (prevType == LETTER) {
                    errors.add(new EnumerationNumberingError(
                            "Inconsistent enum type",
                            new Location(i, 0, line.length())
                    ));
                } else {
                    throw new IllegalArgumentException("Unknown enum type");
                }
            } else if (letterPattern.matcher(line).matches()) {  // it's a letter enum
                var split = line.split("\\)");
                var currentLetter = split[0];
                assert currentLetter.length() == 1;
                var currentChar = currentLetter.charAt(0);
                if (forbiddenLetters.contains(currentChar)) {
//                    TODO: errors.add(new )
                } else {
//                    if (currentChar ) {
//
//                    }
                }
                prevLine = line;
            }
        }
    }
}
