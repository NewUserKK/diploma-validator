package com.wa285.validator.parser.errors.critical;

import com.wa285.validator.parser.errors.Location;

public class PictureNumerationCriticalError extends Critical {
    public PictureNumerationCriticalError(int page, Location location) {
        super("PictureNumerationCriticalError", location);
    }
}
