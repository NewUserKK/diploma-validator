package com.wa285.validator.parser.errors.critical;

import com.wa285.validator.parser.errors.Location;
import org.apache.poi.xwpf.usermodel.XWPFRun;

public class DocumentNumerationCriticalError extends Critical {

    public DocumentNumerationCriticalError(String description, Location location) {
        super(description, location);
    }

    @Override
    public void fix(XWPFRun run) {

    }
}
