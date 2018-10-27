package com.wa285.validator.parser.errors.critical;

import com.wa285.validator.parser.errors.Location;

public class EnumerationCriticalError extends Critical {
    public EnumerationCriticalError(String description, Location location) {
        super(description, location);
    }
}
