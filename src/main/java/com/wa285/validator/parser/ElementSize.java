package com.wa285.validator.parser;

public enum ElementSize {
    DOCUMENT_WIDTH(11909), // TODO: check range (+-20)
    DOCUMENT_HEIGHT(16834),
    LEFT_MARGIN(1699),
    RIGHT_MARGIN(850),
    TOP_MARGIN(1138),
    BOTTOM_MARGIN(1138),
    PARAGRAPH(706);

    private int value;

    ElementSize(int value) {
        this.value = value;
    }

    public int value() {
        return value;
    }
}
