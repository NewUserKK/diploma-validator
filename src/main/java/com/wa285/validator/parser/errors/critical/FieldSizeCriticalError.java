package com.wa285.validator.parser.errors.critical;

import com.wa285.validator.parser.errors.Location;

public class FieldSizeCriticalError extends Critical {

    public FieldSizeCriticalError(int page, Location location) {
        super("FieldSizeCriticalError", location);
    }
}