package com.wa285.validator.parser.errors.critical;

import com.wa285.validator.parser.errors.Error;
import com.wa285.validator.parser.errors.Location;

public abstract class Critical extends Error {

    public Critical(String description, Location location) {
        super(description, location);
    }
}
