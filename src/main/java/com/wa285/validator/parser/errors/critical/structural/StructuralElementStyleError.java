package com.wa285.validator.parser.errors.critical.structural;

import com.wa285.validator.parser.StructuralElement;
import com.wa285.validator.parser.errors.Location;
import com.wa285.validator.parser.errors.critical.Critical;
import org.apache.poi.xwpf.usermodel.XWPFRun;

public abstract class StructuralElementStyleError extends Critical {
    public StructuralElementStyleError(
            StructuralElement element, String description, Location location) {
        super("Structural element: [[" + element.getTitle() + "]] " + description, location);
    }
}
