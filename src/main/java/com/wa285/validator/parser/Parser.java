package com.wa285.validator.parser;

import com.wa285.validator.parser.errors.Error;

import java.io.File;
import java.util.ArrayList;

public class Parser {
    private ArrayList<Error> errors = new ArrayList<>();
    private final File file;

    public Parser(File file) {
        this.file = file;
        parse();
    }

    private void parse() {

    }
}
