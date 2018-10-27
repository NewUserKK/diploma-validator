package com.wa285.validator.parser.errors.critical;

import com.wa285.validator.parser.errors.Critical;

public class CriticalFontColor extends Critical {
    public CriticalFontColor(int page, int position) {
        super("Font's color must be black", page, position);
    }
}
