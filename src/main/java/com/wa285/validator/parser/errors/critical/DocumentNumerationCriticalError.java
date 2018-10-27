package com.wa285.validator.parser.errors.critical;

import com.wa285.validator.parser.errors.Location;

public class DocumentNumerationCriticalError extends Critical {

    public DocumentNumerationCriticalError(String description, Location location) {
        super(description, location);
    }
}
