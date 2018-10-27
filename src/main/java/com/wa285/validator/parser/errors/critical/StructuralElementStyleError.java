package com.wa285.validator.parser.errors.critical;

import com.wa285.validator.parser.StructuralElement;
import com.wa285.validator.parser.errors.Location;
import org.apache.poi.xwpf.usermodel.XWPFRun;

public class StructuralElementStyleError extends Critical {
    public StructuralElementStyleError(
            StructuralElement element, String description, Location location) {
        super("Structural element: [[" + element.getTitle() + "]] " + description, location);
    }

    @Override
    public void fix(XWPFRun run) {

    }
}
