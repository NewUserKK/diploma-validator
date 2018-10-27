package com.wa285.validator.parser.errors.critical.enumeration;

import com.wa285.validator.parser.errors.Location;
import org.apache.poi.xwpf.usermodel.XWPFRun;

public class EnumerationInconsistentTypeError extends EnumerationError {

    public EnumerationInconsistentTypeError(String description, Location location) {
        super(description, location);
    }

    @Override
    public void fix(XWPFRun run) {
        // do nothing
    }
}
