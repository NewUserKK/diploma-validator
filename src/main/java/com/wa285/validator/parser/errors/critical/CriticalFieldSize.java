package com.wa285.validator.parser.errors.critical;

import com.wa285.validator.parser.errors.Critical;

public class CriticalFieldSize extends Critical {

    public CriticalFieldSize(int page, int position) {
        super("", page, position);
    }
}