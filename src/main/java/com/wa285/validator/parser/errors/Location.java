package com.wa285.validator.parser.errors;

import java.math.BigInteger;

public class Location {

    private int paragraphNumber;
    private int startPosition;
    private int endPosition;

    public Location(int paragraphNumber, int startPosition, int endPosition) {
        this.paragraphNumber = paragraphNumber;
        this.startPosition = startPosition;
        this.endPosition = endPosition;
    }

    public int getParagraphNumber() {
        return paragraphNumber;
    }

    public int getStartPosition() {
        return startPosition;
    }

    public int getEndPosition() {
        return endPosition;
    }

    @Override
    public String toString() {
        return "paragraph number: " + paragraphNumber +
                ", start position: " + startPosition +
                ", end position: " + endPosition;
    }
}
