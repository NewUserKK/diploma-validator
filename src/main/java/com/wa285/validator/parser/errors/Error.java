package com.wa285.validator.parser.errors;

public abstract class Error {
    public final String description;
    public final Location location;

    public Error(String description, Location location) {
        this.description = description;
        this.location = location;
    }
}
