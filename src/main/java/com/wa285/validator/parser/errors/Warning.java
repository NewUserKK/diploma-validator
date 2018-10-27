package com.wa285.validator.parser.errors;

public abstract class Warning extends Error {

    public Warning(String description, Location location) {
        super(description, location);
    }
}

