package com.wa285.validator.parser;

/**
 * 6.2.1  Наименования структурных элементов отчета: «СПИСОК ИСПОЛНИТЕЛЕЙ».  «РЕФЕРАТ»,
 * «СОДЕРЖАНИЕ»,  «ТЕРМИНЫ  И ОПРЕДЕЛЕНИЯ», «ПЕРЕЧЕНЬ СОКРАЩЕНИЙ И ОБОЗНАЧЕНИЙ».
 * «ВВЕДЕНИЕ».  «ЗАКЛЮЧЕНИЕ».  «СПИСОК  ИСПОЛЬЗОВАННЫХ  ИСТОЧНИКОВ».  «ПРИЛОЖЕНИЕ»
 */
public enum StructuralElement {
    AUTHORS("СПИСОК ИСПОЛНИТЕЛЕЙ"),
    NAME("РЕФЕРАТ"),
    CONTENT("СОДЕРЖАНИЕ"),
    DEFINITIONS("ТЕРМИНЫ И ОПРЕДЕЛЕНИЯ"),
    NOTATIONS("ПЕРЕЧЕНЬ СОКРАЩЕНИЙ И ОБОЗНАЧЕНИЙ"),
    INTRODUCTION("ВВЕДЕНИЕ"),
    CONCLUSION("ЗАКЛЮЧЕНИЕ"),
    SOURCES("СПИСОК ИСПОЛЬЗОВАННЫХ ИСТОЧНИКОВ"),
    ATTACHMENT("ПРИЛОЖЕНИЕ");

    private String title;

    StructuralElement(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }
}
