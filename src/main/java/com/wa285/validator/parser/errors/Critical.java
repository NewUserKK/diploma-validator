package com.wa285.validator.parser.errors;

public abstract class Critical extends Error {

    public Critical(String description, int page, int position) {
        super(description, page, position);
    }
}
