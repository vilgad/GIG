package com.snippet.gig.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.snippet.gig.exception.BadRequestException;

public enum Priority {
    URGENT("Urgent"),
    LOW("Low"),
    MEDIUM("Medium"),
    HIGH("High");

    private final String value;

    Priority(String value) {
        this.value = value;
    }

    @JsonValue // Tells Jackson to use getValue() method when serializing enum to JSON
    public String getValue() {
        return value;
    }

    @JsonCreator // Tells Jackson to use fromValue() method when deserializing JSON to enum
    public static Priority fromValue(String value) {
        if (value == null || value.isBlank()) {  // Check for null first
            return Priority.LOW;
        }
        for (Priority priority : Priority.values()) {
            if (priority.value.equalsIgnoreCase(value)) {
                return priority;
            }
        }
        throw new BadRequestException("Unknown priority: " + value);
    }
}
