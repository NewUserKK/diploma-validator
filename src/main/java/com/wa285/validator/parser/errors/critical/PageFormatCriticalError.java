package com.wa285.validator.parser.errors.critical;

import com.wa285.validator.parser.errors.Location;

public class PageFormatCriticalError extends Critical {
    public PageFormatCriticalError(int page, Location location) {
        super("Format of project must be A4", location);
    }
}
