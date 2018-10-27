package com.wa285.validator.parser.errors.critical;

import com.wa285.validator.parser.errors.Location;

public class FontSizeCriticalError extends Critical {
    public FontSizeCriticalError(int page, Location location) {
        super("Font's size must be less then 13", location);
    }
}
