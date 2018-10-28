package com.wa285.validator.parser.errors.critical.enumeration;

import com.wa285.validator.parser.errors.Location;
import org.apache.poi.xwpf.usermodel.XWPFRun;

public class WrongHyphenError extends EnumerationError {

    public WrongHyphenError(String description, Location location) {
        super(description, location);
    }

    @Override
    public void fix(XWPFRun run) {
//        run.setText("-" + run.text().substring(1), 0);
    }
}
