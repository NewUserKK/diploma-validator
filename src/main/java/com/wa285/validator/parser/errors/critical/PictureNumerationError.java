package com.wa285.validator.parser.errors.critical;

import com.wa285.validator.parser.errors.Location;
import org.apache.poi.xwpf.usermodel.XWPFRun;

public class PictureNumerationError extends Critical {

    public PictureNumerationError(String description, Location location) {
        super(description, location);
    }

    @Override
    public void fix(XWPFRun run) {

    }
}