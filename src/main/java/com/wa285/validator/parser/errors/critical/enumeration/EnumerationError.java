package com.wa285.validator.parser.errors.critical.enumeration;

import com.wa285.validator.parser.errors.Location;
import com.wa285.validator.parser.errors.critical.Critical;

public abstract class EnumerationError extends Critical {
    public EnumerationError(String description, Location location) {
        super(description, location);
    }
}
