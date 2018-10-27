package com.wa285.validator.parser.errors.critical;

import com.wa285.validator.parser.errors.Error;
import com.wa285.validator.parser.errors.Location;
import org.apache.poi.xwpf.usermodel.XWPFRun;

public abstract class Critical extends Error {

    public Critical(String description, Location location) {
        super(description, location);
    }

    public abstract void fix(XWPFRun run);
}
