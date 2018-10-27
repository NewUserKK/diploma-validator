package com.wa285.validator.parser.errors.critical;

import com.wa285.validator.parser.errors.Location;

public class FontColorCriticalError extends Critical {
    public FontColorCriticalError(int page, Location location) {
        super("Font's color must be black", location);
    }
}
