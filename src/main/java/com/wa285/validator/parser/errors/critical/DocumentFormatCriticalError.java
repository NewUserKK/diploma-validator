package com.wa285.validator.parser.errors.critical;

import com.wa285.validator.parser.errors.Location;

public class DocumentFormatCriticalError extends Critical {

    public DocumentFormatCriticalError(String description, Location location) {
        super(description, location);
    }
}
