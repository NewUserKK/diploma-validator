package com.wa285.validator.parser.errors;

public abstract class Error {
    public final String description;
    public final Location location;

    public Error(String description, Location location) {
        this.description = description;
        this.location = location;
    }

    public Location getLocation() {
        return location;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return description +
                (location != null ? "\n\tat " + location.toString() : "");
    }
}
