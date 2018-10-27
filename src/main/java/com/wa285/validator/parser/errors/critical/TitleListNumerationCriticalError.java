package com.wa285.validator.parser.errors.critical;

import com.wa285.validator.parser.errors.Location;

public class TitleListNumerationCriticalError extends Critical {
    public TitleListNumerationCriticalError(int page, Location location) {
        super("TitleListNumerationCriticalError", location);
    }
}
