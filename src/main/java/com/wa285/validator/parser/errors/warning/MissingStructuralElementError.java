package com.wa285.validator.parser.errors.warning;

import com.wa285.validator.parser.StructuralElement;
import com.wa285.validator.parser.errors.Location;

public class MissingStructuralElementError extends Warning {

    public MissingStructuralElementError(String description, Location location) {
        super(description, location);
    }

    public MissingStructuralElementError(StructuralElement element, Location location) {
        super("Missing structural element: " + element.getTitle(), location);
    }
}
