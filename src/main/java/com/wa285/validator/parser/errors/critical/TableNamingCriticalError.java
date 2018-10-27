package com.wa285.validator.parser.errors.critical;

import com.wa285.validator.parser.errors.Location;

public class TableNamingCriticalError extends Critical {

    public TableNamingCriticalError(String description, Location location) {
        super(description, location);
    }
}
