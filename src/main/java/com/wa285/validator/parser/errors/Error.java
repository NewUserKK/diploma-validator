package com.wa285.validator.parser.errors;

public abstract class Error {
    public final String description;
    public final int page;
    public final int position;

    public Error(String description, int page, int position) {
        this.description = description;
        this.page = page;
        this.position = position;
    }
}
