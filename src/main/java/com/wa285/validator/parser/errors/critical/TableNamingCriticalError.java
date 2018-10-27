package com.wa285.validator.parser.errors.critical;

import com.wa285.validator.parser.errors.Location;

public class TableNamingCriticalError extends Critical {
    public TableNamingCriticalError(int page, Location location) {
        super("TableNamingCriticalError", location);
    }
}
