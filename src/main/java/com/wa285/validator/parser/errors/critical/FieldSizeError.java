package com.wa285.validator.parser.errors.critical;

import com.wa285.validator.parser.errors.GlobalError;
import com.wa285.validator.parser.errors.Location;
import org.apache.poi.xwpf.usermodel.XWPFRun;

import java.math.BigInteger;

import static com.wa285.validator.parser.ElementSize.*;

public class FieldSizeError extends Critical implements GlobalError {

    public FieldSizeError(String description, Location location) {
        super(description, location);
    }

    @Override
    public void fix(XWPFRun run) {
//        var sectPr = run.getDocument().getDocument().getBody().addNewSectPr();
//        var margin = sectPr.addNewPgMar();
        var margin = run.getDocument().getDocument().getBody().getSectPr().getPgMar();
        margin.setLeft(BigInteger.valueOf(LEFT_MARGIN.value().getCenter()));
        margin.setRight(BigInteger.valueOf(RIGHT_MARGIN.value().getCenter()));
        margin.setTop(BigInteger.valueOf(TOP_MARGIN.value().getCenter()));
        margin.setBottom(BigInteger.valueOf(BOTTOM_MARGIN.value().getCenter()));
    }
}