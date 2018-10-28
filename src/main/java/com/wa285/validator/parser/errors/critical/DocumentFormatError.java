package com.wa285.validator.parser.errors.critical;

import com.wa285.validator.parser.errors.GlobalError;
import com.wa285.validator.parser.errors.Location;
import org.apache.poi.xwpf.usermodel.XWPFRun;

import java.math.BigInteger;

import static com.wa285.validator.parser.ElementSize.*;

public class DocumentFormatError extends Critical implements GlobalError {

    public DocumentFormatError(String description, Location location) {
        super(description, location);
    }

    @Override
    public void fix(XWPFRun run) {
        var PgSz = run.getDocument().getDocument().getBody().getSectPr().addNewPgSz();
        PgSz.setH(BigInteger.valueOf(DOCUMENT_HEIGHT.value().getCenter()));
        PgSz.setW(BigInteger.valueOf(DOCUMENT_WIDTH.value().getCenter()));
    }
}
