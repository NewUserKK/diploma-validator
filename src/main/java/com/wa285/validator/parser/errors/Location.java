package com.wa285.validator.parser.errors;

public class Location {

    private final int paragraphNumber;
    private final int runNumber;
    private final int startPosition;
    private final int endPosition;

    public Location(int paragraphNumber, int startPosition, int endPosition, int runNumber) {
        this.paragraphNumber = paragraphNumber;
        this.runNumber = runNumber;
        this.startPosition = startPosition;
        this.endPosition = endPosition;
    }

    public Location(int paragraphNumber, int startPosition, int endPosition) {
        this(paragraphNumber, startPosition, endPosition, 0);
    }

    public int getParagraphNumber() {
        return paragraphNumber;
    }

    public int getRunNumber() {
        return runNumber;
    }

    public int getStartPosition() {
        return startPosition;
    }

    public int getEndPosition() {
        return endPosition;
    }

    @Override
    public String toString() {
        return "Location{" +
                "paragraphNumber=" + paragraphNumber +
                ", runNumber=" + runNumber +
                ", startPosition=" + startPosition +
                ", endPosition=" + endPosition +
                '}';
    }
}
