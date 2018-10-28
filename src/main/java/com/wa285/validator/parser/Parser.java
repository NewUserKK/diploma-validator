package com.wa285.validator.parser;

import com.wa285.validator.parser.errors.Error;
import com.wa285.validator.parser.errors.Location;
import com.wa285.validator.parser.errors.critical.*;
import com.wa285.validator.parser.errors.critical.enumeration.EnumerationNumberingError;
import com.wa285.validator.parser.errors.critical.enumeration.WrongEndingSymbolError;
import com.wa285.validator.parser.errors.critical.enumeration.WrongHyphenError;
import com.wa285.validator.parser.errors.critical.enumeration.WrongStartingSymbolError;
import com.wa285.validator.parser.errors.critical.structural.StructuralElementCenteringError;
import com.wa285.validator.parser.errors.critical.structural.StructuralElementMissingBoldError;
import com.wa285.validator.parser.errors.warning.MissingStructuralElementError;
import org.apache.poi.xwpf.usermodel.ParagraphAlignment;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.xmlbeans.XmlException;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTHpsMeasure;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRPr;

import java.io.*;
import java.util.*;
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

    public List<Error> findErrors(){
        if (errors == null) {
            parse();
        }
        return errors;
    }

    private void parse(){
        errors = new ArrayList<>();
        checkFormat();
        checkStructuralElements();
        checkEnumerations();
    }

    private void checkFormat(){
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

                if (run.getColor() != null && !run.getColor().equals("000000")) {
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

    /*
     * TODO: add dot check in the end
     * TODO: add lower/upper check
     */
    private void checkStructuralElements() {
        Map<StructuralElement, Boolean> structuralElementsCheck = new HashMap<>() {{
            for (var item: StructuralElement.values()) {
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

        for (var item: structuralElementsCheck.keySet()) {
            errors.add(new MissingStructuralElementError(item, null));
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

    private void checkEnumerations() {
        // TODO: complex numbering (1.1. ...)
        // TODO: check for spaces absence after )
        // TODO: check for delimiter after )
        // TODO: REFACTOR THIS PIECE OF SHIT

        final var NONE = 0;
        final var DASH = 1;
        final var DIGIT = 2;
        final var LETTER = 3;

        final var dashPattern = Pattern.compile("^\\p{Space}*\\p{Pd} .*$");  // matches all unicode types of -
        final var digitPattern = Pattern.compile("^\\p{Space}*[1-9]\\d*\\) .*$");  // matches xy...)
        // matches letter with bracket afterwards
        // not that ё is not in range а-я
        final var letterPattern = Pattern.compile("^\\p{Space}*[а-яё]\\) .*$");

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

        var prevLine = "";
        var prevType = NONE;
        var prevStart = "";
        for (int i = 0; i < paragraphs.size(); i++) {
            var paragraph = paragraphs.get(i);
            var line = paragraph.getText().trim();

            /* starts with hyphen */
            if (dashPattern.matcher(line).matches()) {
                if (line.charAt(0) != '-') {
                    errors.add(new WrongHyphenError(
                            "Неправильный тип дефиса",
                            new Location(i, 0, line.length())
                    ));
                }

                if (prevType == DASH) {
                    if (!prevLine.endsWith(",")) {
                        errors.add(new WrongEndingSymbolError(
                                "Простые перечисления должны разделяться запятыми",
                                new Location(i - 1, 0, line.length())
                        ));
                    }
                }

                prevType = DASH;

            /* starts with digit */
            } else if (digitPattern.matcher(line).matches()) {
                var split = line.split("\\)");
                var currentDigit = split[0];
                // TODO: check better, through regex?
                if (prevType == NONE) {
                    if (currentDigit.charAt(0) != '1') {
                        errors.add(new WrongStartingSymbolError(
                                "Нумерация должна начинаться с 1",
                                new Location(i, 0, line.length())
                        ));
                    }

                } else if (prevType == DASH) {
                    if (currentDigit.charAt(0) != '1') {
                        errors.add(new WrongStartingSymbolError(
                                "Вложенные перечисления должны начинаться с 1",
                                new Location(i, 0, line.length())
                        ));
                    }

                } else if (prevType == DIGIT) {
                    if (Integer.parseInt(currentDigit) - 1 != Integer.parseInt(prevStart)) {
                        errors.add(new EnumerationNumberingError(
                                "Не последовательная нумерация списка",
                                new Location(i, 0, line.length())
                        ));
                    }

                } else if (prevType == LETTER) {
                    errors.add(new EnumerationNumberingError(
                            "Смешивание типов нумерации",
                            new Location(i,0, line.length())
                    ));

                } else {
                    throw new IllegalArgumentException("Unknown enum type");
                }
                prevStart = currentDigit;
                prevType = DIGIT;

            /* starts with letter */
            } else if (letterPattern.matcher(line).matches()) {
                var split = line.split("\\)");
                var currentLetter = split[0];
                assert currentLetter.length() == 1;
                var currentChar = currentLetter.charAt(0);

                if (prevType == NONE) {
                    if (currentChar != 'а') {
                        errors.add(new WrongStartingSymbolError(
                                "Нумерация должна начинаться с 'а'",
                                new Location(i, 0, line.length())
                        ));
                    }

                } else if (prevType == DASH) {
                    if (currentChar != '1') {
                        errors.add(new WrongStartingSymbolError(
                                "Вложенные перечисления должны начинаться с 'а'",
                                new Location(i, 0, line.length())
                        ));
                    }
                }

                else if (prevType == DIGIT) {
                    errors.add(new EnumerationNumberingError(
                            "Смешивание типов нумерации",
                            new Location(i,0, line.length())
                    ));

                } else if (prevType == LETTER) {
                    if (!forbiddenLetters.contains(currentChar)) {
                        assert prevStart.length() == 1;
                        // TODO: "я" check?
                        var prevChar = prevStart.charAt(0);
                        boolean isCorrectOrder = false;
                        if (!forbiddenLetters.contains(prevChar)) {
                            if (prevChar == 'е' && currentChar == 'ж' ||
                                    prevChar == 'ж' && currentChar == 'и' ||
                                    prevChar == 'и' && currentChar == 'к' ||
                                    prevChar == 'н' && currentChar == 'п' ||
                                    prevChar == 'ц' && currentChar == 'ш' ||
                                    prevChar == 'щ' && currentChar == 'э' ||
                                    currentChar - 1 == prevChar) {
                                isCorrectOrder = true;
                            }
                        } else {
                            isCorrectOrder = (currentChar - 1 == prevChar ||
                                    prevChar == 'ё' && currentChar == 'ж');
                        }
                        if (!isCorrectOrder) {
                            errors.add(new EnumerationNumberingError(
                                    "Не последовательная нумерация списка",
                                    new Location(i, 0, line.length())
                            ));
                        }
                    } else {
                        errors.add(new EnumerationNumberingError(
                                "В перечислении не должны содержаться буквы: " + forbiddenLetters.toString(),
                                new Location(i, 0, line.length())));
                    }

                } else {
                    throw new IllegalArgumentException("Unknown enum type");
                }
                prevType = LETTER;
                prevStart = currentLetter;

                /* not an enum or it has ended */
            } else {
                if (prevType != NONE) {
                    if (!prevLine.endsWith(".")) {
                        errors.add(new WrongEndingSymbolError(
                                "Перечисления должны заканчиваться точкой",
                                new Location(i - 1, 0, line.length())
                        ));
                    }
                }

                prevType = NONE;
            }
            prevLine = line;
        }
    }
}
