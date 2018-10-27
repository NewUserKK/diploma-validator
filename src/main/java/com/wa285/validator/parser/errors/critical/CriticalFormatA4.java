package com.wa285.validator.parser.errors.critical;

import com.wa285.validator.parser.errors.Critical;

public class CriticalFormatA4 extends Critical {
    public CriticalFormatA4(int page, int position) {
        super("Format of project must be A4", page, position);
    }
}
