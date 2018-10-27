package com.wa285.validator.parser.errors.critical;

import com.wa285.validator.parser.errors.Location;

public class StructuralElementStyleError extends Critical {
    public StructuralElementStyleError(String description, Location location) {
        super(description, location);
    }
}
