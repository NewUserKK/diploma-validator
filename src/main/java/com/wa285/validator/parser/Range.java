package com.wa285.validator.parser;

public class Range
{
    private int low;
    private int high;
    private int center;

    private static final int range = 20;

    public Range(int low, int high){
        this.low = low;
        this.high = high;
        this.center = (low + high) / 2;
    }

    public Range(int center) {
        this.low = center - range;
        this.high = center + range;
        this.center = center;
    }

    public int getCenter() {
        return center;
    }

    public boolean contains(int number){
        return (number >= low && number <= high);
    }
}