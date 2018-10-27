package com.wa285.validator.parser;

public enum ElementSize {
    DOCUMENT_WIDTH(new Range(11909)), // TODO: check range (+-20)
    DOCUMENT_HEIGHT(new Range(16834)),
    LEFT_MARGIN(new Range(1699)),
    RIGHT_MARGIN(new Range(850)),
    TOP_MARGIN(new Range(2578)), // TODO: 1138?
    BOTTOM_MARGIN(new Range(1138)),
    PARAGRAPH(new Range(706));

    private Range value;

    ElementSize(Range value) {
        this.value = value;
    }

    public Range value() {
        return value;
    }
}
