package com.wa285.validator.parser.errors.critical.enumeration;

import com.wa285.validator.parser.errors.Location;
import com.wa285.validator.parser.errors.critical.Critical;
import org.apache.poi.xwpf.usermodel.XWPFRun;

public class EnumerationNumberingError extends Critical {
    public EnumerationNumberingError(String description, Location location) {
        super(description, location);
    }

    @Override
    public void fix(XWPFRun run) {

    }
}
