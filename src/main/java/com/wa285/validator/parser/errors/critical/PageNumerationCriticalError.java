package com.wa285.validator.parser.errors.critical;

import com.wa285.validator.parser.errors.Location;

public class PageNumerationCriticalError extends Critical {

    public PageNumerationCriticalError(int page, Location location) {
        super( "Page numeration error", location);
    }
}
