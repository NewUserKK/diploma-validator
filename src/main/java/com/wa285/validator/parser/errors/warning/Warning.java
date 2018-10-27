package com.wa285.validator.parser.errors.warning;

import com.wa285.validator.parser.errors.Error;
import com.wa285.validator.parser.errors.Location;

public abstract class Warning extends Error {

    public Warning(String description, Location location) {
        super(description, location);
    }

}

