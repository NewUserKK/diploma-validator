package com.wa285.validator.parser.errors.critical.enumeration;

import com.wa285.validator.parser.errors.Location;
import org.apache.poi.xwpf.usermodel.XWPFRun;

public class WrongEndingSymbolError extends EnumerationError {

    public WrongEndingSymbolError(String description, Location location) {
        super(description, location);
    }

    @Override
    public void fix(XWPFRun run) {
        // TODO: "." or ","
    }
}
