package com.wa285.validator.parser.errors.critical;

import com.wa285.validator.parser.errors.Critical;

public class CriticalFontSize extends Critical {
    public CriticalFontSize(int page, int position) {
        super("Font's size must be less then 13", page, position);
    }
}
