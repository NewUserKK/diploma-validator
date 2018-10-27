package com.wa285.validator.parser.errors.critical;

import com.wa285.validator.parser.errors.Critical;

public class CriticalTitleListNumeration extends Critical {
    public CriticalTitleListNumeration(int page, int position) {
        super("", page, position);
    }
}
