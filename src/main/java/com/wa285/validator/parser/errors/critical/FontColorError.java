package com.wa285.validator.parser.errors.critical;

import com.wa285.validator.parser.errors.Location;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.xwpf.usermodel.XWPFRun;

public class FontColorError extends Critical {

    public FontColorError(String description, Location location) {
        super(description, location);
    }

    @Override
    public void fix(XWPFRun run) {
        run.setColor("000000");
    }
}
