package com.wa285.validator.parser.errors.critical;

import com.wa285.validator.parser.errors.Location;

public class FieldSizeCriticalError extends Critical {

    public FieldSizeCriticalError(String description, Location location) {
        super(description, location);
    }
}