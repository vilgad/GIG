package com.snippet.gig.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.snippet.gig.exception.BadRequestException;
import lombok.Getter;

@Getter
public enum Status {
    TODO("To Do"),
    IN_PROGRESS("In Progress"),
    COMPLETED("Completed"),
    REVIEW("Review"),
    Backlog("Backlog"),;

    private final String value;

    Status(String value) {
        this.value = value;
    }

    @JsonValue // Tells Jackson to use getValue() method when serializing enum to JSON
    public String getValue() {
        return value;
    }

    @JsonCreator
    public static Status fromValue(String value) {
        if (value == null || value.isBlank()) {  // Check for null first
            return Status.TODO; // Default value if none provided
        }
        for (Status status : Status.values()) {
            if (status.value.equalsIgnoreCase(value)) {
                return status;
            }
        }
        throw new BadRequestException("Unknown status: " + value);
    }
}
