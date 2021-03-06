package com.wa285.validator.parser.errors.critical.structural;

import com.wa285.validator.parser.StructuralElement;
import com.wa285.validator.parser.errors.Location;
import org.apache.poi.xwpf.usermodel.XWPFRun;

public class StructuralElementMissingBoldError extends StructuralElementStyleError {

    public StructuralElementMissingBoldError(StructuralElement element, String description, Location location) {
        super(element, description, location);
    }

    @Override
    public void fix(XWPFRun run) {
        run.setBold(true);
    }
}
