package com.snippet.gig.requestDto;

import java.util.Objects;

public class UpdateUserRequest {
    private String name;
    private String dob;

    public UpdateUserRequest(String name, String dob) {
        this.name = name;
        this.dob = dob;
    }

    public UpdateUserRequest() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UpdateUserRequest that = (UpdateUserRequest) o;
        return Objects.equals(getName(), that.getName()) && Objects.equals(getDob(), that.getDob());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName(), getDob());
    }

    @Override
    public String toString() {
        return "UpdateUserRequest{" +
                "name='" + name + '\'' +
                ", dob='" + dob + '\'' +
                '}';
    }
}
