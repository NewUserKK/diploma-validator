package com.wa285.validator.parser.errors.critical;

import com.wa285.validator.parser.errors.Critical;

public class CriticalPageNumeration extends Critical {

    public CriticalPageNumeration(String description, int page, int position) {
        super( "Page numeration error", page, position);
    }
}
