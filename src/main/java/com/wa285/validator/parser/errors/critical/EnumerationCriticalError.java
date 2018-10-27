package com.wa285.validator.parser.errors.critical;

import com.wa285.validator.parser.errors.Location;

public class EnumerationCriticalError extends Critical {
    public EnumerationCriticalError(int page, Location location) {
        super("EnumerationCriticalError", location);
    }
}
